package com.example.wifi_manager.viewmodel

import androidx.lifecycle.ViewModel
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.SPUtil

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.viewmodel
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 13:51:48
 * @class describe
 */
open class BaseViewModel:ViewModel(){
    protected val sp by lazy {
        SPUtil.getInstance()
    }
    protected val NET_SUCCESS=200
    protected val context=BaseApplication.application

}