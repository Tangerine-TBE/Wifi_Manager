package com.example.wifi_manager.utils

import android.content.Context
import android.location.LocationManager
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.showToast
import com.tamsiree.rxkit.RxNetTool
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */


fun getConnectWifiName()=WifiUtils.getConnectWifiName()

fun showConnectWifiName(context: Context){
    val connectWifiName = WifiUtils.getConnectWifiName()
    if (connectWifiName != "") {
        if (isOPen(context)) {
            showToast("连上${connectWifiName}!")
        } else {
            showToast(connectWifiName)
        }
    }
}


fun  inner7Day(time: Long): String =
        try {
            val cal = Calendar.getInstance()
            cal.time = Date(time) //设置起时间
            cal.add(Calendar.DATE, 7) //增加一天，这里可以修改，增加一天，增加一年，增加一个月。改变参数。参照Calendar类
            val time: Date = cal.time
            SimpleDateFormat("MM月dd日").format(time)
        } catch (e: Exception) {
            e.printStackTrace()
            SimpleDateFormat("MM月dd日").format(Date().time)
}

fun showLog(msg: String?){
    LogUtils.i("------WJM-----------------------$msg---------------------")
}


fun gpsState(context: Context, action: () -> Unit){
    if (isOPen(context)) {
        action()
    } else {
        showToast("请开启GPS定位服务后再试")
    }
}

/**
 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
 * @param context
 * @return true 表示开启
 */
fun isOPen(context: Context): Boolean {
    val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
    val gps: Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
    val network: Boolean = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    return gps || network
}