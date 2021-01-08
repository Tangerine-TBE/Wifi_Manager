package com.example.wifi_manager.viewmodel

import android.net.wifi.ScanResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
        MutableLiveData<MutableList<ScanResult>>()
    }

    val wifiState by lazy {
        MutableLiveData<WifiState>()
    }





    fun setWifiState(state:WifiState){
        wifiState.value=state
    }


    fun getWifiList(){
        wifiContent.value= WifiUtils.wifiList
    }







}