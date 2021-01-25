package com.example.wifi_manager.utils

import com.example.module_base.utils.showToast
import com.google.zxing.common.StringUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */


fun getConnectWifiName()=WifiUtils.getConnectWifiName()

fun showConnectWifiName(){
    val connectWifiName = WifiUtils.getConnectWifiName()
    if (connectWifiName!="") {
        showToast("连上${connectWifiName}!")
    }
}


fun  inner7Day(time:Long): String =
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