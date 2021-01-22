package com.example.wifi_manager.viewmodel

import androidx.lifecycle.ViewModel
import com.example.module_base.base.BaseApplication

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 13:51:48
 * @class describe
 */
open class BaseViewModel:ViewModel(){
    protected val NET_SUCCESS=200
    protected val context=BaseApplication.application

}