package com.example.wifi_manager.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.WifiManager.*
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.*
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.FragmentHomeBinding

import com.example.wifi_manager.databinding.LayoutStateHomeOpenWifiBinding
import com.example.wifi_manager.domain.ValueNetWorkHint
import com.example.wifi_manager.domain.WifiMessageBean
import com.example.wifi_manager.ui.activity.*
import com.example.wifi_manager.ui.adapter.recycleview.HomeTopAdapter
import com.example.wifi_manager.ui.adapter.recycleview.HomeWifiAdapter
import com.example.wifi_manager.ui.popup.BasePopup
import com.example.wifi_manager.ui.popup.ConnectStatePopup
import com.example.wifi_manager.ui.popup.RemindPopup
import com.example.wifi_manager.ui.popup.WifiConnectPopup
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.HomeViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import com.tamsiree.rxkit.RxNetTool
import com.tamsiree.rxkit.view.RxToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 13:41:05
 * @class describe
 */
class HomeFragment : BaseVmFragment<FragmentHomeBinding, HomeViewModel>() {

    companion object {
        const val REFRESH_HINT = "我已开启，点击刷新"
        const val TOAST_TITLE = "WIFI未开启"
        const val NET_WIFI ="扫描WiFi"
        const val NET_FLOW ="一键连接"
        const val NET_FLOW_HINT ="数据流量"
        const val NET_NOT_CONNECT ="开启网络"
        const val NET_NOT_CONNECT_HINT ="未连接网络"
    }
    private var isUser = false
    private var shareState=true
    private var selectPosition = 0
    private var currentWifiMessages:WifiMessageBean?=null

    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_home
    private val mHomeTopAdapter by lazy {
        HomeTopAdapter()
    }
    private val mNetReceiver by lazy { NetReceiver() }

    private val mWifiListAdapter by lazy {
        HomeWifiAdapter()
    }
    private var mCurrentWifiContent: MutableList<WifiMessageBean> = ArrayList()

    private val mOpenView by lazy {
        binding.mOpenWifiLayout
    }
    private val mCloseView by lazy {
        binding.mCloseWifiLayout
    }
    private val mRemindDialog by lazy {
        RemindPopup(activity)

    }

    private val mConnectWifiPopup by lazy {
        WifiConnectPopup(activity)
    }

    private val mConnectStatePopup by lazy {
        ConnectStatePopup(activity)
    }


    override fun initView() {
        binding.homeData = viewModel
        showOpenView()
        val intentFilter = IntentFilter().apply {
            addAction(NETWORK_STATE_CHANGED_ACTION)
            addAction(WIFI_STATE_CHANGED_ACTION)
            addAction(SCAN_RESULTS_AVAILABLE_ACTION)
        }
        activity?.registerReceiver(mNetReceiver, intentFilter)
        NetWorkHelp.registerNetCallback(netWorkCallback)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun observerData() {
        viewModel.apply {
            val that = this@HomeFragment
            wifiState.observe(that, Observer { state ->
                when (state) {
                    WifiState.ENABLED, WifiState.ENABLING -> {
                        goneView(mCloseView.root)
                        showView(mOpenView.root)
                    }
                    WifiState.DISABLED, WifiState.UNKNOWN -> {
                        goneView(mOpenView.root)
                        showView(mCloseView.root)
                    }
                }
            })

            wifiContentEvent.observe(that, Observer { result ->
                when (result.state) {
                    WifiContentState.REFRESH -> {
                        binding.mOpenWifiLayout.mSmartRefreshLayout.finishRefresh()
                        RxToast.normal("发现了${result.list.size}个wifi")
                    }
                }
                mCurrentWifiContent = result.list
                if (RxNetTool.isWifiConnected(requireContext())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mCurrentWifiContent.removeIf{ it.wifiName==getConnectWifiName() }
                    }
                }
                mWifiListAdapter.setList(mCurrentWifiContent)
                mOpenView.timeAttend.text= Html.fromHtml("...    WIFI管家已陪伴您<font color='#ffffff'><big><big><big><big>     ${calLastedTime(Date(mSaveSP.getLong(Constants.FIRST_TIME)), Date())}天    <small><small><small><small></font>...")
                mCurrentWifiContent.forEach {
               //     LogUtils.i("---wifiContent---------${it}-----------")
                }

            })

            errorConnectCount.observe(that, Observer { count ->
                if (count > 4) {
                    mConnectTimeOut.cancel()
                    dismissErrorPopup()
                }else{
                     mConnectTimeOut.start()
                }
            })
            connectError.observe(that, Observer { success->
             //   if (!success) { dismissErrorPopup() }
            })

            connectingCount.observe(that, Observer { count ->
                mScope.launch (Dispatchers.Main) {
                    withContext(Dispatchers.Main){
                    mConnectStatePopup?.apply {
                    when (count) {
                         1 -> setConnectState(StepState.ONE)
                         2->setConnectState(StepState.TWO)
                         3->setConnectState(StepState.THREE)
                        in 4..5->setConnectState(StepState.FOUR)
                        in 6..10->setConnectState(StepState.FIVE)
                    }
                        LogUtils.i("-----ConnectProgressView--------------$count----")
                    }
                    }
                    delay(3000)
                }
            })

        }
    }

    private val mConnectTimeOut by lazy {
        startCountDown(8000, 1000, {
            dismissErrorPopup()
        }) {
            LogUtils.i("---errorConnectCount------------------")
        }
    }


    private var isWifi=false
    inner class NetReceiver : BroadcastReceiver() {
        /**
         * WIFI_STATE_DISABLED    WLAN已经关闭
         * WIFI_STATE_DISABLING   WLAN正在关闭
         * WIFI_STATE_ENABLED     WLAN已经打开
         * WIFI_STATE_ENABLING    WLAN正在打开
         * WIFI_STATE_UNKNOWN     未知
         */
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                SCAN_RESULTS_AVAILABLE_ACTION -> {
                    LogUtils.i("wifi列表发生变化")
                }
                NETWORK_STATE_CHANGED_ACTION -> {
                    LogUtils.i("NETWORK_STATE_CHANGED_ACTION")
                    val info: NetworkInfo? = intent.getParcelableExtra(EXTRA_NETWORK_INFO)
                    when {
                        NetworkInfo.State.DISCONNECTED == info?.state -> {//wifi没连接上
                            if (isUser) {
                                viewModel.setConnectErrorCount(1)
                            }
                            LogUtils.i("wifi没连接上");
                        }
                        NetworkInfo.State.CONNECTED == info?.state -> {//wifi连接上了
                            LogUtils.i("wifi以连接")
                            mConnectTimeOut.cancel()
                            isWifi=true
                            mScope.launch(Dispatchers.Main) {
                                if (isUser) {
                                    mConnectStatePopup?.setConnectState(StepState.FIVE)
                                    delay(500)
                                    mConnectStatePopup?.dismissPopup()
                                    currentWifiMessages?.let {
                                        if (shareState and (it.wifiProtectState != HomeViewModel.OPEN) and (getConnectWifiName()==it.wifiName)) {
                                            viewModel.shareWifiInfo(it)
                                        }
                                    }
                                }

                                viewModel.getWifiList(WifiContentState.NORMAL)

                            }
                            viewModel.setCurrentNetState(ValueNetWorkHint(getConnectWifiName(), NET_WIFI))
                            showConnectWifiName()
                        }
                        NetworkInfo.State.CONNECTING == info?.state -> {//正在连接
                            LogUtils.i("wifi正在连接");
                            if (isUser) {
                                viewModel.setConnectingCount(1)
                            }

                        }
                    }

                }
                WIFI_STATE_CHANGED_ACTION -> {
                    val state = intent.getIntExtra(EXTRA_WIFI_STATE, 0)
                    when (state) {
                        WIFI_STATE_DISABLED -> {
                            viewModel.setWifiState(WifiState.DISABLED)
                            LogUtils.i(" WLAN已经关闭")
                        }
                        WIFI_STATE_DISABLING -> {
                            viewModel.setWifiState(WifiState.DISABLING)
                            LogUtils.i(" WLAN正在关闭")

                        }
                        WIFI_STATE_ENABLED -> {
                            viewModel.setWifiState(WifiState.ENABLED)
                            viewModel.getWifiList(WifiContentState.NORMAL)
                            LogUtils.i(" WLAN已经打开")
                        }
                        WIFI_STATE_ENABLING -> {
                            viewModel.setWifiState(WifiState.ENABLING)
                            LogUtils.i(" WLAN正在打开")

                        }
                        WIFI_STATE_UNKNOWN -> {
                            viewModel.setWifiState(WifiState.UNKNOWN)
                            LogUtils.i(" 未知")
                        }
                    }
                }

            }

        }
    }
    private fun dismissErrorPopup() {
        mConnectStatePopup?.dismissPopup()
        showToast("换一个试试吧亲！")
    }
    private fun showOpenView() {
        mOpenView.apply {
            setStatusBar(activity, mHomeTopContainer, LayoutType.LINEARLAYOUT)
            //顶部功能
            mHomeTopAdapter.setList(DataProvider.homeTopList)
            mHomeTopContainer.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            mHomeTopContainer.adapter = mHomeTopAdapter
            //wifi列表
            mHomeWifiContainer.layoutManager = LinearLayoutManager(activity)
            mHomeWifiContainer.adapter = mWifiListAdapter
            mWifiListAdapter.addChildClickViewIds(R.id.mWifiInfo)
            //设置下拉刷新的头
            mSmartRefreshLayout.setRefreshHeader(MaterialHeader(activity))




        }

        mCloseView.apply {
            //加下划线
            val content = SpannableString(REFRESH_HINT);
            content.setSpan(UnderlineSpan(), 5, REFRESH_HINT.length, 0);
            mRefreshWifi.text = content
        }
    }
    override fun initEvent() {
        mConnectStatePopup?.setOnDismissListener {
            mConnectStatePopup.mOutValueAnimator.start()
            isUser=false
        }

        mCloseView.apply {
            //开启wifi
            mOpenWifi.setOnClickListener {
                mScope.launch(Dispatchers.Main) {
                    delay(1000)
                    WifiUtils.openWifi()
                }

            }
            //刷新wifi
            mRefreshWifi.setOnClickListener {
                if (WifiUtils.isWifiEnable) {
                    viewModel.getWifiList(WifiContentState.NORMAL)
                } else {
                    RxToast.normal(TOAST_TITLE)
                }
            }
        }

        mOpenView.apply {
            //下拉刷新监听
            mSmartRefreshLayout.setOnRefreshListener {
                viewModel.getWifiList(WifiContentState.REFRESH)
            }
            //扫描wifi监听
            mScanWifi.setOnClickListener {
                mSmartRefreshLayout.autoRefresh()
            }
            // wifi列表子view监听
            mWifiListAdapter.apply {
                setOnItemChildClickListener { adapter, view, position ->
                    when (view.id) {
                        R.id.mWifiInfo -> {
                            mCurrentWifiContent?.let {
                                if (it.size > 0) {
                                    toOtherActivity<WifiInfoViewActivity>(activity) {
                                        putExtra(ConstantsUtil.WIFI_NAME_KEY, it[position].wifiName)
                                        putExtra(ConstantsUtil.WIFI_LEVEL_KEY, it[position].wifiSignalState)
                                        putExtra(ConstantsUtil.WIFI_PROTECT_KEY, it[position].wifiProtectState)
                                    }
                                }
                            }

                        }
                    }
                }
                //wifi列表主view监听
                setOnItemClickListener { adapter, view, position ->
                    mCurrentWifiContent?.let {
                        isUser = true
                        viewModel.clearConnectCount()

                        selectPosition = position
                        val wifiMessageBean = it[position]
                        if (RxNetTool.isWifiConnected(requireContext())) {
                            mRemindDialog.showPopupView(mSmartRefreshLayout, y = -150)
                        } else {
                            viewModel.connectAction(wifiMessageBean) {
                                showPwdPopup(wifiMessageBean)
                            }
                        }
                    }
                }
            }

            //wifi已经连接，温馨提示框
            mRemindDialog?.apply {
                setOnActionClickListener(object : BasePopup.OnActionClickListener {
                    override fun sure() {
                        mCurrentWifiContent?.let {
                            val wifiMessage = it[selectPosition]
                            viewModel.connectAction(wifiMessage) {
                                showPwdPopup(wifiMessage)
                            }
                        }
                    }

                    override fun cancel() {

                    }
                })

            }
            //有密码的wifi连接框监听
            mConnectWifiPopup?.apply {
                setOnActionClickListener(object : BasePopup.OnActionClickListener {
                    override fun sure() {
                        mCurrentWifiContent?.let {
                            val wifiPwd = getWifiPwd()
                            shareState = getShareState()
                            val wifiMessageBean = it[selectPosition]
                            if (wifiPwd.length < 8) {
                                showToast("WiFi密码必须是8位及以上")
                            } else {
                                dismiss()
                                beginConnectWifi(wifiMessageBean, false, wifiPwd)
                            }
                        }
                    }

                    override fun cancel() {

                    }
                })
            }
            //顶部的item点击监听
            mHomeTopAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 -> toOtherActivity<CheckDeviceViewActivity>(activity) {}
                    1 -> toOtherActivity<SafetyCheckActivity>(activity) {}
                    2 -> {
                    }
                    3 -> toOtherActivity<WifiProtectViewActivity>(activity) {}
                    4 -> if (RxNetTool.isWifiConnected(requireContext()))
                        toOtherActivity<SignalUpActivity>(activity) {}
                    else
                        showToast(ConstantsUtil.NO_CONNECT_WIFI)
                    }


            }

        }


    }
    private fun LayoutStateHomeOpenWifiBinding.beginConnectWifi(wifiMessage: WifiMessageBean, open: Boolean, wifiPwd: String = "") {
        mConnectStatePopup?.apply {
            setState(wifiMessage.wifiProtectState == HomeViewModel.OPEN)
            setConnectName(wifiMessage.wifiName)
            showPopupView(mSmartRefreshLayout, y = -150)


            viewModel.connectWifi(wifiMessage, open, wifiPwd)
            currentWifiMessages=wifiMessage
            currentWifiMessages?.let {
                it.wifiPwd=wifiPwd
            }

        }
    }
    private fun LayoutStateHomeOpenWifiBinding.showPwdPopup(wifiMessage: WifiMessageBean) {
        mConnectStatePopup?.setConnectState(StepState.ONE)
        if (wifiMessage.shareState) {
            beginConnectWifi(wifiMessage, false,wifiMessage.wifiPwd)
        } else {
            if (wifiMessage.wifiProtectState == HomeViewModel.OPEN) {
                beginConnectWifi(wifiMessage, true)
            } else {
                mConnectWifiPopup?.apply {
                    setWifiName(wifiMessage.wifiName)
                    showPopupView(mSmartRefreshLayout)
                }
            }
        }
    }

    private val netWorkCallback by lazy {
        object :ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                LogUtils.i("网络连接了")

            }

            override fun onLost(network: Network) {
                super.onLost(network)
                LogUtils.i("网络中断")
                if (!isWifi) {
                    viewModel.setCurrentNetState(ValueNetWorkHint(NET_NOT_CONNECT_HINT, NET_NOT_CONNECT))
                }
                isWifi=false
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    LogUtils.i("WIFI已连接")
                    viewModel.setCurrentNetState(ValueNetWorkHint(getConnectWifiName(), NET_WIFI))
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    LogUtils.i("移动网络已连接")
                    if (!isWifi) {
                        viewModel.setCurrentNetState(ValueNetWorkHint(NET_FLOW_HINT, NET_FLOW))
                    }
                }
            }
        }
    }







    override fun release() {
        activity?.unregisterReceiver(mNetReceiver)
        NetWorkHelp.unregisterNetCallback(netWorkCallback)
    }

}