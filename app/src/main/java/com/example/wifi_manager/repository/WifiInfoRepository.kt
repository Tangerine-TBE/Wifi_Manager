package com.example.wifi_manager.repository

import com.example.wifi_manager.utils.RetrofitClient

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.repository
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/20 18:54:49
 * @class describe
 */
object WifiInfoRepository {
    fun shareWifi(wifiName: String, wifiMacAddress: String, pwd: String, encryptionWay: String)
    = RetrofitClient.createWifiManager().shareWifiInfo(wifiName,wifiMacAddress,pwd,encryptionWay)
}