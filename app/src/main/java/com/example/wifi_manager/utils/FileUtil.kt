package com.example.wifi_manager.utils

import android.os.Environment
import com.example.module_base.base.BaseApplication
import java.io.File

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/13 15:42:40
 * @class describe
 */
object FileUtil {



    fun getFilePath(): String =BaseApplication.application.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator+"down_test"




}