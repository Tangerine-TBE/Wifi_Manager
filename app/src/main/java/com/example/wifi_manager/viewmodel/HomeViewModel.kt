package com.example.wifi_manager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wifi_manager.domain.RefreshWifiEvent
import com.example.wifi_manager.domain.WifiMessage
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
        MutableLiveData<MutableList<WifiMessage>>()
    }
    val wifiState by lazy {
        MutableLiveData<WifiState>()
    }
    val wifiContentEvent by lazy {
        MutableLiveData<RefreshWifiEvent>()
    }


    private val oldWifiMessages:MutableList<WifiMessage> =ArrayList()
    private val currentWifiMessages:MutableList<WifiMessage> =ArrayList()

    fun setWifiState(state:WifiState){
        wifiState.value=state
    }

    fun getWifiList(state:WifiContentState){


        currentWifiMessages?.apply {
            val wifiList = WifiUtils.wifiList.filter { it.SSID!="" }
            if (wifiList.isNotEmpty()) {
                currentWifiMessages.clear()
                wifiList.forEach { add(WifiMessage(it.SSID, it.BSSID, it.capabilities, it.level)) }

                oldWifiMessages?.clear()
                oldWifiMessages?.addAll(this)

                setWifiContent(state,this)
            } else {
               if (oldWifiMessages?.size>0) setWifiContent(state,oldWifiMessages)
            }
        }
    }

    private fun setWifiContent(state: WifiContentState,list: MutableList<WifiMessage>) {
        wifiContentEvent.value = RefreshWifiEvent(state, list.size)
        wifiContent.value = list
    }
}