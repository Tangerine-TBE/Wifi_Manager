package com.example.module_user.domain

import com.example.module_user.utils.NetState

/**
 * @name Wifi_Manager
 * @class name：com.example.module_user.domain
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/29 14:14:42
 * @class describe
 */
data class ValueResult(val state:NetState, val msg:String)