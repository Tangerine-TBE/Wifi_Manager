package com.example.wifi_manager.domain

import com.example.wifi_manager.utils.WifiContentState

/**
 * @author: 铭少
 * @date: 2021/1/10 0010
 * @description：
 */
data class ValueRefreshWifi(var state:WifiContentState, var refreshSize:Int=0) {
}