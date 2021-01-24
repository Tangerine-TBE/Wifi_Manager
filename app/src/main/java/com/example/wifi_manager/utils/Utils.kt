package com.example.wifi_manager.utils

import com.example.module_base.utils.showToast

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