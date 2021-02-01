package com.example.wifi_manager.domain

import android.net.wifi.WifiConfiguration

/**
 * @author: 铭少
 * @date: 2021/1/10 0010
 * @description：
 */
data class WifiMessageBean(var wifiName:String, var wifiMacAddress:String, var  encryptionWay:String,
                           var wifiLevel:Int, var wifiSignalState:String, var wifiProtectState: String,
                           var wifiPwd:String="", var saveWifiPwdState:Boolean=false,var saveWifiPwdInfo:WifiConfiguration?=null,
                            var shareState:Boolean=false,var sort:Int=4
                           ) {
}