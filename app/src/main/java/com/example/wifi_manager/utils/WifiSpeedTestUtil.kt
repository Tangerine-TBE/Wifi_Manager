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


    fun netWorkLevel(speed:Float?)= when (speed?:0f) {
            in 0f..0.25f -> "1M～2M"
            in 0.25f..0.375f ->  "2M～3M"
            in 0.375f..0.5f ->  "3M～4M"
            in 0.5f..0.75f ->  "4M～6M"
            in 0.75f..1f ->  "6M～8M"
            in 1f .. 1.25f ->  "8M～10M"
            in 1.25f..1.5f ->  "10M～12M"
            in 1.5f..2.5f -> "12M～20M"
            in 2.5f..6.25f ->  "20M～50M"
            in 6.25f..100f ->  "50M～100M"
            else->""
    }
}