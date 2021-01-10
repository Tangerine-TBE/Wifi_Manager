package com.example.wifi_manager.domain

/**
 * @author: 铭少
 * @date: 2021/1/10 0010
 * @description：
 */
data class WifiMessage(var wifiName:String,var wifiMacAddress:String,var  encryptionWay:String,var wifiLevel:Int) {
}