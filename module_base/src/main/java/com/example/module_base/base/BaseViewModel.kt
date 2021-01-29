package com.example.module_base.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.SPUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

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
    companion object{
        const val NET_ERROR ="网络错误"
    }

    protected val NET_SUCCESS=200
    protected val context=BaseApplication.application


    protected  fun doRequest(success:suspend()->Unit, error:(errorInfo:String)->Unit){
        viewModelScope.launch (Dispatchers.IO){
            try {
                success()
            } catch (e: Exception) {
                error( e.toString())
            }
    }
    }
}