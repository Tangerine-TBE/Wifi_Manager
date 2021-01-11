package com.example.wifi_manager.utils

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 15:50:25
 * @class describe
 */
enum class WifiState {
    /**
     * WIFI_STATE_DISABLED    WLAN已经关闭
     * WIFI_STATE_DISABLING   WLAN正在关闭
     * WIFI_STATE_ENABLED     WLAN已经打开
     * WIFI_STATE_ENABLING    WLAN正在打开
     * WIFI_STATE_UNKNOWN     未知
     */
   DISABLED,DISABLING, ENABLED,ENABLING,UNKNOWN

}

enum class WifiContentState(){
    NORMAL,REFRESH
}