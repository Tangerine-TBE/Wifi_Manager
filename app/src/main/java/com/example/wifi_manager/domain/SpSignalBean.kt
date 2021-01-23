package com.example.wifi_manager.domain

/**
 * @author: 铭少
 * @date: 2021/1/23 0023
 * @description：
 */
data class SpSignalBean(var currentWifiName:String,var oldLevel:Int,var newLevel:Int){
    constructor():this("",0,0)
}