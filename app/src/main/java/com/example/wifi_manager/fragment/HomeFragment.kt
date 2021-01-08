package com.example.wifi_manager.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.WifiManager.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.MarginStatusBarUtil
import com.example.wifi_manager.R
import com.example.wifi_manager.adapter.HomeTopAdapter
import com.example.wifi_manager.adapter.HomeWifiAdapter
import com.example.wifi_manager.databinding.FragmentHomeBinding
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.WifiState
import com.example.wifi_manager.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.fragment
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

    override fun initView() {
        mBinding.data=viewModel
        mBinding.lifecycleOwner=this
        MarginStatusBarUtil.setStatusBar(activity,mHomeTopContainer,2)
        mHomeTopContainer.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        mHomeTopAdapter.setList(DataProvider.homeTopList)
        mHomeTopContainer.adapter = mHomeTopAdapter


        mHomeWifiContainer.layoutManager = LinearLayoutManager(activity)
        mHomeWifiContainer.adapter=mWifiListAdapter



        val intentFilter = IntentFilter().apply {
            addAction(NETWORK_STATE_CHANGED_ACTION)
            addAction(WIFI_STATE_CHANGED_ACTION)
            addAction(SCAN_RESULTS_AVAILABLE_ACTION)
        }
        activity?.registerReceiver(mNetReceiver,intentFilter)


    }


    override fun observerData() {
        viewModel.wifiState.observe(this, Observer {state->

        })
        viewModel.wifiContent.observe(this, Observer { result ->
            LogUtils.i("---wifiContent---------${result.size}-----------")
            mWifiListAdapter.setList(result)
            result.forEach {
                LogUtils.i("---wifiContent---------${it}-----------")
            }
        })

    }

    override fun initEvent() {

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
                            viewModel.getWifiList()
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