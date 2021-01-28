package com.example.wifi_manager.livedata

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.wifi.WifiManager
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.LogUtils

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/28 16:25:59
 * @class describe
 */
object LocationLiveData:BaseLiveData<Boolean>() {

    private  val mGpsOpenReceiver by lazy {
        GpsOpenReceiver()
    }


    override fun onActive() {
        super.onActive()
        val intentFilter = IntentFilter().apply {
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        }
        BaseApplication.application?.registerReceiver(mGpsOpenReceiver, intentFilter)
    }

    override fun onInactive() {
        super.onInactive()
        BaseApplication.application?.unregisterReceiver(mGpsOpenReceiver)
    }


    class GpsOpenReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent?) {
            //------------------------位置--------------------
            val locationManager = context.getSystemService(Service.LOCATION_SERVICE) as LocationManager
            value= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

    }


}