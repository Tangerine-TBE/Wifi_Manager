package com.example.wifi_manager.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager.*
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.MarginStatusBarUtil
import com.example.wifi_manager.R
import com.example.wifi_manager.ui.activity.WifiInfoActivity
import com.example.wifi_manager.ui.adapter.HomeTopAdapter
import com.example.wifi_manager.ui.adapter.HomeWifiAdapter
import com.example.wifi_manager.databinding.FragmentHomeBinding
import com.example.wifi_manager.domain.WifiMessage
import com.example.wifi_manager.utils.*
import com.example.wifi_manager.viewmodel.HomeViewModel
import com.scwang.smart.refresh.header.MaterialHeader
import com.tamsiree.rxkit.view.RxToast
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_state_home_close_wifi.*
import kotlinx.android.synthetic.main.layout_state_home_open_wifi.*


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 13:41:05
 * @class describe
 */
class HomeFragment : BaseVmFragment<FragmentHomeBinding, HomeViewModel>() {
    override fun getViewModelClass(): Class<HomeViewModel> { return HomeViewModel::class.java }
    override fun getChildLayout(): Int = R.layout.fragment_home
    private val mHomeTopAdapter by lazy { HomeTopAdapter()
    }
    private val mNetReceiver by lazy { NetReceiver() }

    private val mWifiListAdapter by  lazy {
        HomeWifiAdapter()
    }
    private var mCurrentWifiContent:MutableList<WifiMessage> = ArrayList()


    override fun initView() {
        binding.homeData=viewModel
        showOpenView()
        val intentFilter = IntentFilter().apply {
            addAction(NETWORK_STATE_CHANGED_ACTION)
            addAction(WIFI_STATE_CHANGED_ACTION)
            addAction(SCAN_RESULTS_AVAILABLE_ACTION)
        }
        activity?.registerReceiver(mNetReceiver,intentFilter)
    }


    override fun observerData() {
        viewModel.apply {
            val that = this@HomeFragment
            wifiState.observe(that, Observer { state ->
                when (state) {
                    WifiState.ENABLED, WifiState.ENABLING -> {
                        goneView(mCloseWifiLayout)
                        showView(mOpenWifiLayout)
                    }
                    WifiState.DISABLED, WifiState.UNKNOWN -> {
                        goneView(mOpenWifiLayout)
                        showView(mCloseWifiLayout)
                    }
                }
            })
            wifiContent.observe(that, Observer { result ->
                LogUtils.i("---wifiContent---------${result.size}-----------")
                mCurrentWifiContent=result
                mWifiListAdapter.setList(result)
                result.forEach {
                    LogUtils.i("---wifiContent---------${it}-----------")
                }
            })

            wifiContentEvent.observe(that, Observer { event->
                when(event.state){
                    WifiContentState.REFRESH->{
                        mSmartRefreshLayout.finishRefresh()
                        RxToast.normal("发现了${event.refreshSize}个wifi")
                    }
                }
            })
        }
    }

    private fun showOpenView() {
        MarginStatusBarUtil.setStatusBar(activity, mHomeTopContainer, 1)
        //顶部功能
        mHomeTopContainer.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        mHomeTopAdapter.setList(DataProvider.homeTopList)
        mHomeTopContainer.adapter = mHomeTopAdapter
        //wifi列表
        mHomeWifiContainer.layoutManager = LinearLayoutManager(activity)
        mHomeWifiContainer.adapter = mWifiListAdapter
        mWifiListAdapter.addChildClickViewIds(R.id.mWifiInfo)
        //下拉刷新头
        mSmartRefreshLayout.setRefreshHeader(MaterialHeader(activity))
        //加下划线
        val str="我已开启，点击刷新"
        val content =  SpannableString(str);
        content.setSpan(UnderlineSpan(), 5, str.length, 0);
        mRefreshWifi.text=content

    }

    override fun initEvent() {
        //下拉刷新
        mSmartRefreshLayout.setOnRefreshListener {
            viewModel.getWifiList(WifiContentState.REFRESH)
        }
        //扫描wifi
        mScanWifi.setOnClickListener {
            mSmartRefreshLayout.autoRefresh()
        }

        //开启wifi
        mOpenWifi.setOnClickListener {
            WifiUtils.openWifi()
        }

        //刷新wifi
        mRefreshWifi.setOnClickListener {
            if (WifiUtils.isWifiEnable) {
                viewModel.getWifiList(WifiContentState.NORMAL)
            } else {
                RxToast.normal("WIFI未开启")
            }
        }
        mWifiListAdapter.setOnItemChildClickListener  { adapter, view, position ->
            when(view.id){
                R.id.mWifiInfo-> {
                    mCurrentWifiContent?.let {
                        if (it.size>0){
                            toOtherActivity<WifiInfoActivity>(activity){
                                putExtra(ConstantsUtil.WIFI_NAME_KEY,it[position].wifiName)
                                putExtra(ConstantsUtil.WIFI_LEVEL_KEY,it[position].wifiSignalState)
                                putExtra(ConstantsUtil.WIFI_PROTECT_KEY,it[position].wifiProtectState)
                            }
                        }
                    }

                }
            }

        }


    }


    inner class NetReceiver:BroadcastReceiver() {
        /**
         * WIFI_STATE_DISABLED    WLAN已经关闭
         * WIFI_STATE_DISABLING   WLAN正在关闭
         * WIFI_STATE_ENABLED     WLAN已经打开
         * WIFI_STATE_ENABLING    WLAN正在打开
         * WIFI_STATE_UNKNOWN     未知
         */
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                SCAN_RESULTS_AVAILABLE_ACTION->{
                    LogUtils.i("wifi列表发生变化")
                }
                NETWORK_STATE_CHANGED_ACTION -> {
                    LogUtils.i("NETWORK_STATE_CHANGED_ACTION")

                }
                WIFI_STATE_CHANGED_ACTION -> {
                    val state = intent.getIntExtra(EXTRA_WIFI_STATE, 0)
                    when(state){
                        WIFI_STATE_DISABLED ->{
                            viewModel.setWifiState(WifiState.DISABLED)
                            LogUtils.i(" WLAN已经关闭")
                        }
                        WIFI_STATE_DISABLING ->{
                            viewModel.setWifiState(WifiState.DISABLING)
                            LogUtils.i(" WLAN正在关闭")

                        }
                        WIFI_STATE_ENABLED ->{
                            viewModel.setWifiState(WifiState.ENABLED)
                            viewModel.getWifiList(WifiContentState.NORMAL)
                            LogUtils.i(" WLAN已经打开")
                        }
                        WIFI_STATE_ENABLING ->{
                            viewModel.setWifiState(WifiState.ENABLING)
                            LogUtils.i(" WLAN正在打开")

                        }
                        WIFI_STATE_UNKNOWN ->{
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
    }

}