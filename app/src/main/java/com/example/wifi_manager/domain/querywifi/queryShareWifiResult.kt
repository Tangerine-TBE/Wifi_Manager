package com.example.wifi_manager.domain.querywifi

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.domain.querywifi
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 17:21:05
 * @class describe
 */
data class queryShareWifiResult(
    val code: Int,
    val `data`: Data,
    val msg: String
)

data class Data(
    val address: String,
    val method: String,
    val name: String,
    val password: String
)