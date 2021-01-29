package com.example.module_user.domain

import com.example.module_user.domain.login.LoginBean

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.domain
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/29 15:12:16
 * @class describe
 */
data class ValueUserInfo(val loginState:Boolean,val userInfo: LoginBean?=null)
