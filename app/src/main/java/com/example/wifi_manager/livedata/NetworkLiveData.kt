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
import kotlinx.coroutines.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 10:25:28
 * @class describe
 */
object NetworkLiveData:BaseLiveData<NetworkState>() {
    private var networkCallback: ConnectivityManager.NetworkCallback = NetworkCallbackImpl()
    private var request: NetworkRequest = NetworkRequest.Builder().build()
    private var manager: ConnectivityManager = BaseApplication.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    override fun onActive() {
        super.onActive()
        manager.registerNetworkCallback(request, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        manager.unregisterNetworkCallback(networkCallback)
    }

    class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(NetworkState.CONNECT)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(NetworkState.NONE)
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
                        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            postValue(NetworkState.WIFI)
                        } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            postValue(NetworkState.CELLULAR)
                        }
            }

    }
}