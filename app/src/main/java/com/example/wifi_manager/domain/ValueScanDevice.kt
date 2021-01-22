package com.example.wifi_manager.domain

import com.example.wifi_manager.utils.ProgressState

/**
 * @author: 铭少
 * @date: 2021/1/17 0017
 * @description：
 */
data class ValueScanDevice(val scanState:ProgressState, val deviceContent:MutableList<DeviceBean>) {
}