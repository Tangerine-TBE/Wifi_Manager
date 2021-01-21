package com.example.wifi_manager.domain

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.domain
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 14:01:50
 * @class describe
 */
data class RequestWifiBean(
    val code: Int,
    val `data`: Data,
    val msg: String
)

data class Data(
    val list: List<WifiInfo>,
    val total: Int
)

data class WifiInfo(
    val address: String,
    val method: String,
    val name: String,
    val password: String
)


data class CancelShareResult(
    val code: Int,
    val `data`: Any,
    val msg: String
)

