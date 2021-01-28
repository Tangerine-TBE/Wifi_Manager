package com.example.wifi_manager.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager.*
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.*
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.FragmentHomeBinding
import com.example.wifi_manager.domain.ValueNetWorkHint
import com.example.wifi_manager.domain.WifiMessageBean
import com.example.wifi_manager.ui.activity.*
import com.example.wifi_manager.ui.adapter.recycleview.HomeTopAdapter
import com.example.wifi_manager.ui.adapter.recycleview.HomeWifiAdapter
import com.example.wifi_manager.ui.popup.ConnectStatePopup
import com.example.wifi_manager.ui.popup.RemindPopup
import com.example.wifi_manager.ui.popup.WifiConnectPopup
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.HomeViewModel
import com.feisukj.cleaning.ui.activity.StrongAccelerateActivity
import com.scwang.smart.refresh.header.MaterialHeader
import com.tamsiree.rxkit.RxNetTool
import com.tamsiree.rxkit.view.RxToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        const val NET_WIFI = "扫描WiFi"
        const val NET_FLOW = "一键连接"
        const val NET_FLOW_HINT = "数据流量"
        const val NET_NOT_CONNECT = "开启网络"
        const val NET_NOT_CONNECT_HINT = "未连接网络"
    }

    private var isUser = false
    private var shareState = true
    private var selectPosition = 0
    private var currentWifiMessages: WifiMessageBean? = null
    private val mHomeTopAdapter by lazy { HomeTopAdapter() }
    private val mNetReceiver by lazy { NetReceiver() }
    private val mWifiListAdapter by lazy { HomeWifiAdapter() }
    private var mCurrentWifiContent: MutableList<WifiMessageBean> = ArrayList()
    private val mOpenView by lazy { binding.mOpenWifiLayout }
    private val mCloseView by lazy { binding.mCloseWifiLayout }
    private val mRemindDialog by lazy { RemindPopup(activity) }
    private val mConnectWifiPopup by lazy { WifiConnectPopup(activity) }
    private val mConnectStatePopup by lazy { ConnectStatePopup(activity) }
    private val mConnectTimeOut by lazy {
        startCountDown(8000, 1000, {
            dismissErrorPopup()
        }) {
            LogUtils.i("---errorConnectCount------------------")
        }
    }

    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_home
    override fun initView() {
        binding.homeData = viewModel
        showOpenView()
        val intentFilter = IntentFilter().apply {
            addAction(NETWORK_STATE_CHANGED_ACTION)
            addAction(WIFI_STATE_CHANGED_ACTION)
            addAction(SCAN_RESULTS_AVAILABLE_ACTION)
            addAction(SUPPLICANT_STATE_CHANGED_ACTION)
        }
        activity?.registerReceiver(mNetReceiver, intentFilter)
        NetWorkHelp.registerNetCallback(netWorkCallback)

        viewModel.checkProtectTimeOut()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun observerData() {
        viewModel.apply {
            val that = this@HomeFragment
            protectTimeOut.observe(that, {
                if (it) {
                    mScope.launch(Dispatchers.Main) {
                        delay(2000)
                        RxToast.warning("WiFi保镖的保护已经取消")
                    }
                }
            })


            wifiState.observe(that, { state ->
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

            wifiContentEvent.observe(that, { result ->

                when (result.state) {
                    WifiContentState.REFRESH -> {
                        binding.mOpenWifiLayout.mSmartRefreshLayout.finishRefresh()
                        RxToast.normal("发现了${result.list.size}个wifi")
                    }
                }
                mCurrentWifiContent = result.list
                if (RxNetTool.isWifiConnected(requireContext())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mCurrentWifiContent.removeIf { it.wifiName == getConnectWifiName() }
                    }
                }
                mWifiListAdapter.setList(mCurrentWifiContent)

                mOpenView.timeAttend.apply {
                    visibility = View.VISIBLE
                    text = Html.fromHtml(
                            "...    WIFI管家已陪伴您<font color='#ffffff'><big><big><big><big>     ${
                                calLastedTime(
                                        Date(System.currentTimeMillis()),
                                        Date(sp.getLong(Constants.FIRST_TIME))
                                )
                            }天    <small><small><small><small></font>..."
                    )
                }


            })

            currentNetWorkName.observeForever {
                mOpenView.mNetWorkName.text = it.currentNet
                mOpenView.mScanWifi.text = it.currentAction
            }


        }
    }

    private var isWifi = false
    private var connectBegin = false

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
                //开放：SCANNING ASSOCIATING ASSOCIATED COMPLETED
                //加密：SCANNING ASSOCIATING ASSOCIATED FOUR_WAY_HANDSHAKE  DISCONNECTED
                SUPPLICANT_STATE_CHANGED_ACTION -> {
                    val supplicantState = intent.getParcelableExtra(EXTRA_NEW_STATE) as? SupplicantState
                    supplicantState?.apply {
                        LogUtils.i("SUPPLICANT_STATE_CHANGED_ACTION---------------------$supplicantState")
                        when (this) {
                            SupplicantState.SCANNING -> {
                                if (isUser) {
                                    mConnectStatePopup.setConnectState(StepState.ONE)
                                }
                            }
                            SupplicantState.ASSOCIATING -> {
                                if (isUser) {
                                    mConnectStatePopup.setConnectState(StepState.TWO)
                                    connectBegin = true
                                }
                            }
                            SupplicantState.ASSOCIATED -> {
                                if (isUser) {
                                    mConnectStatePopup.setConnectState(StepState.THREE)
                                }
                            }
                            SupplicantState.COMPLETED -> {
                                if (isUser) {
                                    mConnectStatePopup.setConnectState(StepState.FIVE)
                                    connectBegin = false
                                }
                            }
                            SupplicantState.DISCONNECTED -> {
                                if (connectBegin) {
                                    if (isUser)
                                        dismissErrorPopup()
                                }
                                connectBegin = false
                            }
                        }
                    }
                }

                SCAN_RESULTS_AVAILABLE_ACTION -> {
                //    LogUtils.i("wifi列表发生变化")
                }
                NETWORK_STATE_CHANGED_ACTION -> {
                    val info: NetworkInfo? = intent.getParcelableExtra(EXTRA_NETWORK_INFO)
                    when {
                        NetworkInfo.State.CONNECTED == info?.state -> {//wifi连接上了
                        //    LogUtils.i("wifi以连接")
                            mConnectTimeOut.cancel()
                            isWifi = true
                            if (isUser) {
                                mConnectStatePopup?.dismiss()
                                currentWifiMessages?.let {
                                    if (shareState and (it.wifiProtectState != HomeViewModel.OPEN) and (getConnectWifiName() == it.wifiName)) {
                                        viewModel.shareWifiInfo(it)
                                    }
                                }
                            }
                            viewModel.getWifiList(WifiContentState.NORMAL)


                            viewModel.setCurrentNetState(
                                    ValueNetWorkHint(
                                            getConnectWifiName(),
                                            NET_WIFI
                                    )
                            )
                            showConnectWifiName()

                            //用于Wifi保镖检查
                            if (WifiUtils.getCipherType()) {
                                sp.putBoolean(ConstantsUtil.SP_WIFI_PROTECT_STATE, false)
                            } else {
                                sp.putBoolean(ConstantsUtil.SP_WIFI_PROTECT_STATE, true)
                            }

                            sp.putBoolean(ConstantsUtil.SP_SIGNAL_SATE, false)
                        }

                        NetworkInfo.State.DISCONNECTED == info?.state -> {//wifi没连接上
                            if (isUser) viewModel.setConnectErrorCount(1)
                       //     LogUtils.i("wifi没连接上");
                        }

                        NetworkInfo.State.CONNECTING == info?.state -> {//正在连接
                            if (isUser) viewModel.setConnectingCount(1)
                    //       LogUtils.i("wifi正在连接");
                        }
                    }

                }
                WIFI_STATE_CHANGED_ACTION -> {
                    val state = intent.getIntExtra(EXTRA_WIFI_STATE, 0)
                    when (state) {
                        WIFI_STATE_DISABLED -> {
                            viewModel.setWifiState(WifiState.DISABLED)
                            mOpenView.timeAttend.visibility = View.GONE
                            LogUtils.i(" WLAN已经关闭")
                        }
                        WIFI_STATE_DISABLING -> {
                            viewModel.setWifiState(WifiState.DISABLING)
                            LogUtils.i(" WLAN正在关闭")

                        }
                        WIFI_STATE_ENABLED -> {
                            viewModel.getWifiList(WifiContentState.NORMAL)
                            viewModel.setWifiState(WifiState.ENABLED)
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



    private fun showOpenView() {
        mOpenView.apply {
            setStatusBar(activity, mHomeTopContainer, LayoutType.LINEARLAYOUT)
            //顶部功能
            mHomeTopAdapter.setList(DataProvider.homeTopList)
            mHomeTopContainer.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
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
        mConnectStatePopup?.apply {
            setOnDismissListener {
                mConnectTimeOut.cancel()
                mOutValueAnimator?.start()
                isUser = false
            }
        }

        mCloseView.apply {
            //开启wifi
            mOpenWifi.setOnClickListener {
                WifiUtils.openWifi()
            }
            //刷新wifi
            mRefreshWifi.setOnClickListener {
                if (WifiUtils.isWifiEnable) {
                    viewModel.setWifiState(WifiState.ENABLED)
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
                                        putExtra(
                                                ConstantsUtil.WIFI_LEVEL_KEY,
                                                it[position].wifiSignalState
                                        )
                                        putExtra(
                                                ConstantsUtil.WIFI_PROTECT_KEY,
                                                it[position].wifiProtectState
                                        )
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
                        currentWifiMessages = wifiMessageBean
                        if (RxNetTool.isWifiConnected(requireContext())) {
                            mRemindDialog.showPopupView(mSmartRefreshLayout, y = -150)
                        } else {
                            viewModel.connectAction(wifiMessageBean) {
                                connectStateAction(wifiMessageBean)
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
                                connectStateAction(wifiMessage)
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
                        mCurrentWifiContent?.let { it ->
                            val pwd= getWifiPwd()
                            shareState = getShareState()
                            val wifiMessageBean = it[selectPosition]
                            if (pwd.length < 8) {
                                showToast("WiFi密码必须是8位及以上")
                            } else {
                                dismiss()

                                showConnectPopup(wifiMessageBean)
                                viewModel.connectWifi(wifiMessageBean, false,pwd)
                                currentWifiMessages?.let { it.wifiPwd = pwd }
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
                    0 ->   startActivity(StrongAccelerateActivity.getIntent(requireContext()))
                    1 -> if (RxNetTool.isWifiConnected(requireContext())) toOtherActivity<SafetyCheckActivity>(activity) {}
                    else
                        showToast(ConstantsUtil.NO_CONNECT_WIFI)
                    2 -> {
                        if (RxNetTool.isWifiConnected(requireContext())) toOtherActivity<SpeedTestViewActivity>(activity) {}
                        else
                            showToast(ConstantsUtil.NO_CONNECT_WIFI)
                    }
                    3 -> {
                        if (RxNetTool.isWifiConnected(requireContext())) {
                            if (sp.getBoolean(ConstantsUtil.SP_WIFI_PROTECT_OPEN)) {
                                toOtherActivity<WifiProtectInfoViewActivity>(activity) {}
                            } else {
                                toOtherActivity<WifiProtectViewActivity>(activity) {}
                            }
                        } else
                            showToast(ConstantsUtil.NO_CONNECT_WIFI)
                    }
                    4 -> if (RxNetTool.isWifiConnected(requireContext()))
                        toOtherActivity<SignalUpActivity>(activity) {}
                    else
                        showToast(ConstantsUtil.NO_CONNECT_WIFI)
                }
            }

        }


    }

    private fun showConnectPopup(wifiMessage: WifiMessageBean) {
        mConnectStatePopup?.apply {
            // 初始化弹出的连接状态
            setConnectState(StepState.ONE)
            setState(wifiMessage.wifiProtectState == HomeViewModel.OPEN)
            setConnectName(wifiMessage.wifiName)
            showPopupView(mOpenView.mSmartRefreshLayout, y = -150)
        }
    }






    private fun dismissErrorPopup() {
        mConnectStatePopup?.dismiss()
        showToast("换一个试试吧亲！")

        if (saveConnectSate) {
            currentWifiMessages?.apply {
                isUser=true
                showPwConnectPopup(this)
            }
        }
        if (shareConnectSate) {
            isUser=true
            currentWifiMessages?.apply { showPwConnectPopup(this) }
        }

        saveConnectSate=false
        shareConnectSate=false

    }
    private var  saveConnectSate=false
    private var  shareConnectSate=false
    fun connectStateAction(wifiMessage: WifiMessageBean) {
        wifiMessage.apply {
            if (wifiProtectState == HomeViewModel.OPEN) {
                showConnectPopup(this)
                viewModel.connectWifi(this, true)
            } else {
                if (saveWifiPwdState) {
                    saveConnectSate=true
                    showConnectPopup(this)
                    viewModel.savePwdConnectWifi(wifiName)



                }else{
                    if (shareState) {
                        shareConnectSate=true
                        showConnectPopup(this)
                        viewModel.connectWifi(this,false,wifiPwd)

                    } else {
                        showPwConnectPopup(this)
                    }
                }
            }
        }
    }

    private fun showPwConnectPopup(wifiMessage:WifiMessageBean) {
        mConnectWifiPopup?.apply {
            setWifiName(wifiMessage.wifiName)
            showPopupView(mOpenView.mSmartRefreshLayout)
        }
    }


    private val netWorkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
          //      LogUtils.i("网络连接了")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
         //       LogUtils.i("网络中断")
                if (!isWifi) {
                    viewModel.setCurrentNetState(
                            ValueNetWorkHint(
                                    NET_NOT_CONNECT_HINT,
                                    NET_NOT_CONNECT
                            )
                    )
                }
                isWifi = false
            }

            override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
             //       LogUtils.i("WIFI已连接")
                    viewModel.setCurrentNetState(ValueNetWorkHint(getConnectWifiName(), NET_WIFI))
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
           //         LogUtils.i("移动网络已连接")
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