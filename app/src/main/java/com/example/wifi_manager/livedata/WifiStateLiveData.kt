package com.example.wifi_manager.livedata

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.EXTRA_WIFI_STATE
import android.net.wifi.WifiManager.WIFI_STATE_DISABLED
import androidx.navigation.compose.navArgument
import com.example.module_base.base.BaseApplication
import com.example.wifi_manager.utils.WifiState


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 10:25:28
 * @class describe
 */
object WifiStateLiveData:BaseLiveData<WifiState>() {

    private  val mWifiStateReceiver by lazy {
        WifiStateReceiver()
    }

    override fun onActive() {
        super.onActive()
        val intentFilter = IntentFilter().apply {
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        }
        BaseApplication.application?.registerReceiver(mWifiStateReceiver, intentFilter)
    }


    override fun onInactive() {
        super.onInactive()
        BaseApplication.application?.unregisterReceiver(mWifiStateReceiver)
        value=WifiState.UNKNOWN
    }

     class WifiStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                    when(intent.getIntExtra(EXTRA_WIFI_STATE, 0)){
                        WIFI_STATE_DISABLED -> {
                            value = WifiState.DISABLED
                    }
                }
                }
            }
        }

    }


}