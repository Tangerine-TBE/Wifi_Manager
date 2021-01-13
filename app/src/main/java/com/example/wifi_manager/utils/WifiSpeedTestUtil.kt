package com.example.wifi_manager.utils

import android.net.TrafficStats




/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/13 11:35:18
 * @class describe
 */
object WifiSpeedTestUtil {

    fun getTotalRxBytes(): Long {  //获取总的接受字节数，包含Mobile和WiFi等

        return if (TrafficStats.getTotalRxBytes() == TrafficStats.UNSUPPORTED.toLong()) 0 else TrafficStats.getTotalRxBytes() / 1024
    }

    fun getMobileRxBytes(): Long {  //获取通过Mobile连接收到的字节总数，不包含WiFi
        return if (TrafficStats.getMobileRxBytes() == TrafficStats.UNSUPPORTED.toLong()) 0 else TrafficStats.getMobileRxBytes() / 1024
    }

    fun getTotalTxBytes(): Long {  //总的发送字节数，包含Mobile和WiFi等
        return if (TrafficStats.getTotalTxBytes() == TrafficStats.UNSUPPORTED.toLong()) 0 else TrafficStats.getTotalTxBytes() / 1024
    }





}