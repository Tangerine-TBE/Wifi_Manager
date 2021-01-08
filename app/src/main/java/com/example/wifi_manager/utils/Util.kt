package com.example.wifi_manager.utils

import android.content.Context
import android.content.Intent

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/8 13:32:53
 * @class describe
 */



inline fun <reified T>toOtherActivity(context: Context?,block:Intent.()->Unit){
    val intent = Intent(context,T::class.java)
    intent.block()
    context?.startActivity(intent)
}