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

    companion object{
        const val WPA2="WPA2"
        const val WPA_AND_WPA2="WPA/WPA2"
        const val OPEN="开放"

        const val LEVEL_HIGH="强"
        const val LEVEL_MIDDLE="一般"
        const val LEVEL_LOW="弱"
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
        mCurrentWifiMessageBeans?.let { list->
            val wifiList = WifiUtils.wifiList.filter { it.SSID!="" }
            if (wifiList.isNotEmpty()) {
                list.clear()
                wifiList.forEach {
                    list.add(WifiMessageBean(it.SSID, it.BSSID, it.capabilities, it.level,wifiSignalState(it.level),wifiProtectState(it.capabilities)))
                }

                list.sortWith(compareBy({ it.wifiProtectState.length},{it.wifiName}))

                mOldWifiMessageBeans?.clear()
                mOldWifiMessageBeans?.addAll(list)
                setWifiContent(state,list)
            } else {
                if (mOldWifiMessageBeans?.size > 0) setWifiContent(state, mOldWifiMessageBeans)
            }
        }
    }

    private fun setWifiContent(state: WifiContentState,list: MutableList<WifiMessageBean>) {
        wifiContentEvent.value = ValueRefreshWifi(state, list)
    }


    private fun wifiSignalState(level: Int) =
        if (level>=-50){
            LEVEL_HIGH
        }else if (level<=51 && level>=-70){
            LEVEL_MIDDLE
        }else {
            LEVEL_LOW
        }

    private fun wifiProtectState(capabilities: String) = when {
        capabilities.startsWith("[WPA2") -> {
            WPA2
        }
        capabilities.startsWith("[WPA")  -> {
            WPA_AND_WPA2
        }
        else -> {
            OPEN
        }
    }

}