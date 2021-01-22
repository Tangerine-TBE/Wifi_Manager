package com.example.wifi_manager.livedata

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.LogUtils
import com.example.wifi_manager.utils.NetworkState
import com.example.wifi_manager.utils.WifiUtils
import kotlinx.coroutines.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 10:25:28
 * @class describe
 */
object WifiSignalLevelLiveData:BaseLiveData<Int>() {

    override fun onActive() {
        super.onActive()
        value= when(WifiUtils.getConnectWifiSignalLevel()){
            in 0 downTo -20 ->100
            in -20 downTo -50 ->99
            in -50 downTo -60 ->80
            in -60 downTo -70 -> 60
            in -70 downTo -1000 -> 40
            else -> 100
        }
    }


}