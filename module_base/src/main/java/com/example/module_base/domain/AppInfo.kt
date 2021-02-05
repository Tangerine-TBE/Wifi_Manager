package com.example.module_base.domain

import android.graphics.drawable.Drawable

/**
 * @name Wifi_Manager
 * @class name：com.example.module_base.domain
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/22 10:08:12
 * @class describe
 */
data class AppInfo(var appName:String?="",var packageName:String?="",var versionName:String?="",var versionCode:Int=0,var appIcon:Drawable?)
{
    constructor():this("","","",0,null)
}

