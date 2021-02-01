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
    fun shareWifi(wifiName: String, wifiMacAddress: String, wifiPwd: String, wifiEncryptionWay: String)
    = RetrofitClient.createWifiManager().shareWifiInfo(wifiName,wifiMacAddress,wifiPwd,wifiEncryptionWay)

    fun cancelShareWifi(wifiName: String?, wifiMacAddress: String?, wifiPwd: String?)
    =RetrofitClient.createWifiManager().cancelShareWifiInfo(wifiName,wifiMacAddress,wifiPwd)

    fun queryShareWifi(wifiName: String?, wifiMacAddress: String?, wifiPwd: String?)=   RetrofitClient.createWifiManager().queryShareWifiInfo(wifiName,wifiMacAddress,wifiPwd)



    suspend fun getShareWifiList(wifiMacAddress: String?)
        =RetrofitClient.createWifiManager().getShareWifiContent(wifiMacAddress)



}