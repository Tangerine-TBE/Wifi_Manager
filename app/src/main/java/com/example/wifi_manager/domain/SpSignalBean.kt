package com.example.wifi_manager.domain

/**
 * @author: 铭少
 * @date: 2021/1/23 0023
 * @description：
 */
data class SpSignalBean(var currentWifiName:String,var oldLevel:Int,var newLevel:Int,var upLevel:Int=0){
    constructor():this("",0,0)
}