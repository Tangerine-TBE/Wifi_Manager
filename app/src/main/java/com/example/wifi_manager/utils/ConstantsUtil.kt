package com.example.wifi_manager.utils

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/11 15:08:03
 * @class describe
 */
object ConstantsUtil {

    //-----------------Intent的key--------------
    //通用
    const val WIFI_NAME_KEY = "WIFI_NAME_KEY"


    //扫描页
    const val ZXING_RESULT_KEY = "ZXING_RESULT_KEY"

    //WIFI信息页
    const val WIFI_LEVEL_KEY = "WIFI_LEVEL_KEY"
    const val WIFI_PROTECT_KEY = "WIFI_PROTECT_KEY"

    //WIFI测速结果
    const val WIFI_DELAY_KEY = "WIFI_DELAY_KEY"
    const val WIFI_DOWN_LOAD_KEY = "WIFI_DOWN_LOAD_KEY"

    //信号增强
    const val SIGNAL_SATE = "SIGNAL_SATE"

    //分享页
    const val WIFI_SHARE_ACTION = "WIFI_SHARE_ACTION"


    //ping值 测试网址
    const val PING_URL = "https://www.baidu.com"


    //Toast
    const val NO_CONNECT_WIFI = "WiFi未连接"
    const val DIS_WIFI = "WiFi已关闭"


    //-----------------------------SP------------------
    //信号增强
    const val SP_SIGNAL_INFO = "SP_SIGNAL_INFO"
    const val SP_SIGNAL_SATE = "SP_SIGNAL_SATE"


    //是否加密
    const val SP_WIFI_PROTECT_STATE = "SP_WIFI_PROTECT_STATE"


    //wifi保镖
    const val SP_WIFI_PROTECT_OPEN = "SP_WIFI_PROTECT_OPEN"
    const val SP_WIFI_PROTECT_TIME = "SP_WIFI_PROTECT_TIME"
    const val SP_WIFI_PROTECT_NAME = "SP_WIFI_PROTECT_NAME"
    const val SP_WIFI_PROTECT_DAY = "SP_WIFI_PROTECT_DAY"



    //-------------------------通知-----------------
    const val ACTION_WIFI_CONNECT_CANCEL = "ACTION_WIFI_CONNECT_CANCEL"
    const val NOTIFICATION_WIFI_ID = "NOTIFICATION_WIFI_ID"


}