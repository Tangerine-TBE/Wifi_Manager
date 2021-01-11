package com.example.module_tool.base

import android.app.Application
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import com.example.module_base.base.BaseApplication
import com.example.module_tool.utils.PackageUtils

object BaseConstant {
    val mainHandler:Handler by lazy { Handler(Looper.getMainLooper()) }
    val application: Application by lazy { BaseApplication.application}
    val packageName:String by lazy { application.packageName }
    var isForeground: Boolean = false
    var isFromStart: Boolean = false
    val channel:String by lazy { PackageUtils.getAppMetaData(application, "CHANNEL") }
    val versionName by lazy { application.packageManager.getPackageInfo(application.packageName, PackageManager.GET_ACTIVITIES).versionName }

}