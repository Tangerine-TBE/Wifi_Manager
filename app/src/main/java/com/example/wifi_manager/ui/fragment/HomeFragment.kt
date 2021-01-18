package com.example.wifi_manager.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.net.*
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.*
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.setStatusBar
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.FragmentHomeBinding
import com.example.wifi_manager.databinding.LayoutStateHomeOpenWifiBinding
import com.example.wifi_manager.domain.WifiMessageBean
import com.example.wifi_manager.extensions.noFinishShow
import com.example.wifi_manager.ui.activity.CheckDeviceViewActivity
import com.example.wifi_manager.ui.activity.SafetyCheckActivity
import com.example.wifi_manager.ui.activity.WifiInfoViewActivity
import com.example.wifi_manager.ui.activity.WifiProtectViewActivity
import com.example.wifi_manager.ui.adapter.recycleview.HomeTopAdapter
import com.example.wifi_manager.ui.adapter.recycleview.HomeWifiAdapter
import com.example.wifi_manager.ui.popup.BasePopup
import com.example.wifi_manager.ui.popup.WifiConnectPopup
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.HomeViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.view.dialog.RxDialogSureCancel
import kotlinx.android.synthetic.main.layout_state_home_open_wifi.*
import kotlinx.android.synthetic.main.popup_wifi_connect_window.view.*


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 13:41:05
 * @class describe
 */
class HomeFragment : BaseVmFragment<FragmentHomeBinding, HomeViewModel>() {

    companion object{
        const val DIALOG_TIP="温馨提示"
        const val DIALOG_CONTENT="您当前已连接WiFi，确认连接其他WiFi？"
        const val REFRESH_HINT="我已开启，点击刷新"
        const val TOAST_TITLE="WIFI未开启"

    }


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
        RxDialogSureCancel(activity).apply {
            titleView.apply {
                textSize = 18f
                setTextColor(Color.BLACK)
                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            setTitle(DIALOG_TIP)
            setContent(DIALOG_CONTENT)
        }

    }

    private val mConnectWifiPopup by lazy {
        WifiConnectPopup(activity)
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
        registerNetworkCallback(requireContext())

    }

    override fun observerData() {
        viewModel.apply {
            val that = this@HomeFragment
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
                        mSmartRefreshLayout.finishRefresh()
                        RxToast.normal("发现了${result.list.size}个wifi")
                    }

                }

                mCurrentWifiContent = result.list
                mWifiListAdapter.setList(mCurrentWifiContent)
                mCurrentWifiContent.forEach {
                    LogUtils.i("---wifiContent---------${it}-----------")
                }

            })
        }
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

    private var selectPosition = 0

    override fun initEvent() {
        mCloseView.apply {
            //开启wifi
            mOpenWifi.setOnClickListener {
                BaseApplication.mHandler.postDelayed({  WifiUtils.openWifi()},1000)

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
                        selectPosition = position
                        val wifiMessageBean = it[position]
                        if (isWifiConnected(requireContext())) {
                                mRemindDialog.noFinishShow(activity)
                        } else {
                            connectAction(wifiMessageBean)
                        }
                    }
                }
            }

            //wifi已经连接，温馨提示框
            mRemindDialog.apply {
                setSureListener {
                    dismiss()
                    mCurrentWifiContent?.let {
                        connectAction(it[selectPosition])
                    }
                }

                setCancelListener { dismiss() }
            }
            //有密码的wifi连接框监听
            mConnectWifiPopup?.apply {
                setOnActionClickListener(object : BasePopup.OnActionClickListener {
                    override fun sure() {
                        mCurrentWifiContent?.let {
                            val wifiPwd = getWifiPwd()
                            val shareState = getShareState()
                            val wifiMessageBean = it[selectPosition]
                            WifiUtils.connectWifiPws(wifiMessageBean.wifiName, wifiPwd)
                            LogUtils.i("--mConnectWifiPopup------$wifiPwd-----------------$shareState------------")
                        }
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
                    4 -> {
                    }
                }
            }

        }

    }

    private fun LayoutStateHomeOpenWifiBinding.connectAction(wifiMessage: WifiMessageBean) {
        if (wifiMessage.wifiProtectState == HomeViewModel.OPEN) {
            WifiUtils.connectWifiNoPws(wifiMessage.wifiName)
        } else {
            mConnectWifiPopup?.apply {
                setWifiName(wifiMessage.wifiName)
                showPopupView(mSmartRefreshLayout, Gravity.CENTER)
            }
        }
    }


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
                 //   Log.e("=====", "--NetworkInfo--" + info.toString());
                    when {
                        NetworkInfo.State.DISCONNECTED == info?.state -> {//wifi没连接上
                            LogUtils.i("wifi没连接上");

                        }
                        NetworkInfo.State.CONNECTED == info?.state -> {//wifi连接上了
                            LogUtils.i("wifi以连接");

                        }
                        NetworkInfo.State.CONNECTING == info?.state -> {//正在连接
                            LogUtils.i("wifi正在连接");
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


    override fun release() {
        activity?.unregisterReceiver(mNetReceiver)
        unregisterNetworkCallback(requireContext())
    }

    /**
     * Android10监听网络变化广播
     *
     */

    private val  netWorkCallback by lazy {
        object:ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                LogUtils.i(" onLost")

            }
            override fun onAvailable(network: Network) {
                LogUtils.i(" onAvailable")
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                LogUtils.i(" onLosing")
            }

            override fun onUnavailable() {
                LogUtils.i(" onUnavailable")
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                LogUtils.i(" onLinkPropertiesChanged")
            }

            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                LogUtils.i(" onBlockedStatusChanged")
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                LogUtils.i(" onCapabilitiesChanged")

            }
        }
    }




    // 注册网络监听回调
    private fun registerNetworkCallback(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         NetworkRequest.Builder().apply {
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            cm?.registerNetworkCallback(build(), netWorkCallback)
        }
    }


    // 注销网络监听回调
    private fun unregisterNetworkCallback(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm?.unregisterNetworkCallback(netWorkCallback)
    }


}