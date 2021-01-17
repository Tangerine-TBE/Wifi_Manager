package com.example.wifi_manager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wifi_manager.domain.ValueRefreshWifi
import com.example.wifi_manager.domain.WifiMessageBean
import com.example.wifi_manager.utils.WifiContentState
import com.example.wifi_manager.utils.WifiState
import com.example.wifi_manager.utils.WifiUtils

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 13:42:18
 * @class describe
 */
class HomeViewModel:ViewModel() {

    val wifiContent by lazy {
        MutableLiveData<MutableList<WifiMessageBean>>()
    }
    val wifiState by lazy {
        MutableLiveData<WifiState>()
    }
    val wifiContentEvent by lazy {
        MutableLiveData<ValueRefreshWifi>()
    }


    private val mOldWifiMessageBeans:MutableList<WifiMessageBean> =ArrayList()
    private val mCurrentWifiMessageBeans:MutableList<WifiMessageBean> =ArrayList()

    fun setWifiState(state:WifiState){
        wifiState.value=state
    }

    fun getWifiList(state:WifiContentState){
        mCurrentWifiMessageBeans?.apply {
            val wifiList = WifiUtils.wifiList.filter { it.SSID!="" }
            if (wifiList.isNotEmpty()) {
                mCurrentWifiMessageBeans.clear()
                wifiList.forEach {
                    add(WifiMessageBean(it.SSID, it.BSSID, it.capabilities, it.level,wifiSignalState(it.level),wifiProtectState(it.capabilities)))
                }

                mOldWifiMessageBeans?.clear()
                mOldWifiMessageBeans?.addAll(this)
                setWifiContent(state,this)
            } else {
                if (mOldWifiMessageBeans?.size > 0) setWifiContent(
                    state,
                    mOldWifiMessageBeans
                )
            }
        }
    }

    private fun setWifiContent(state: WifiContentState,list: MutableList<WifiMessageBean>) {
        wifiContentEvent.value = ValueRefreshWifi(state, list.size)
        wifiContent.value = list
    }


    private fun wifiSignalState(level: Int) =
        if (level>=-50){
            "强"
        }else if (level<=51 && level>=-70){
            "一般"
        }else {
            "弱"
        }

    private fun wifiProtectState(capabilities: String) = when {
        capabilities.startsWith("[WPA2") -> {
            "WPA2"
        }
        capabilities.startsWith("[WPA")  -> {
            "WPA/WPA2"
        }
        else -> {
            "开放"
        }
    }

}