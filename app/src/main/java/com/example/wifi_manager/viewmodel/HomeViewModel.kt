package com.example.wifi_manager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.domain.ValueRefreshWifi
import com.example.wifi_manager.domain.WifiMessageBean
import com.example.wifi_manager.utils.WifiContentState
import com.example.wifi_manager.utils.WifiState
import com.example.wifi_manager.utils.WifiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        const val PROTECT_WAY_ONE="[WPA2"
        const val PROTECT_WAY_TWO="[WPA"

    }


    val wifiState by lazy {
        MutableLiveData<WifiState>()
    }
    val wifiContentEvent by lazy {
        MutableLiveData<ValueRefreshWifi>()
    }

    val errorConnectCount by lazy {
        MutableLiveData<Int>()
    }

    val connectingCount by lazy {
        MutableLiveData<Int>()
    }

    val currentNetWorkName by lazy {
        MutableLiveData<String>()
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

    fun setNetWorkName(name:String){
        currentNetWorkName.value=name
    }


    private fun wifiSignalState(level: Int) =
            when(level){
                in 0 downTo -20-> LEVEL_HIGH
                in -20 downTo -50-> LEVEL_HIGH
                in -50 downTo -60->  LEVEL_MIDDLE
                in -60 downTo -70->  LEVEL_MIDDLE
                in -70 downTo -1000->  LEVEL_LOW
                else->LEVEL_HIGH
            }

    private fun wifiProtectState(capabilities: String) = when {
        capabilities.startsWith(PROTECT_WAY_ONE) -> {
            WPA2
        }
        capabilities.startsWith(PROTECT_WAY_TWO)  -> {
            WPA_AND_WPA2
        }
        else -> {
            OPEN
        }
    }

     fun connectWifi(wifiMessage: WifiMessageBean, open:Boolean, wifiPwd:String=""){
            if (open) {
                WifiUtils.connectWifiNoPws(wifiMessage.wifiName)
            } else {
                WifiUtils.connectWifiPws(wifiMessage.wifiName, wifiPwd)
            }
    }


    fun connectAction(wifiMessage: WifiMessageBean, showPopupAction:()->Unit){
        if (wifiMessage.wifiProtectState == OPEN) {
            connectWifi(wifiMessage,true)
        }
        showPopupAction()
    }


    private var mConnectErrorCount=0
    private var mConnectingCount=0
    fun setConnectErrorCount(count:Int){
        mConnectErrorCount+=count
        errorConnectCount.value=mConnectErrorCount
        LogUtils.i("---没连接上---------${mConnectErrorCount}-----------")
    }
    fun setConnectingCount(count:Int){
        mConnectingCount+=count
        connectingCount.value=mConnectingCount
        LogUtils.i("---正在连接---------${mConnectingCount}-----------")
    }


    fun clearConnectCount(){
        mConnectErrorCount=0
        mConnectingCount=0
    }


}