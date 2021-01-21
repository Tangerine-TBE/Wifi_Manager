package com.example.wifi_manager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.module_base.base.BaseApplication
import com.example.wifi_manager.livedata.NetworkLiveData

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 11:48:52
 * @class describe
 */
object NetWorkHelp {
    private var manager: ConnectivityManager = BaseApplication.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var request: NetworkRequest = NetworkRequest.Builder().build()
     fun registerNetCallback(callback:ConnectivityManager.NetworkCallback){
         manager.registerNetworkCallback(request,callback)
    }

    fun unregisterNetCallback(callback:ConnectivityManager.NetworkCallback){
        manager.unregisterNetworkCallback(callback)
    }

}