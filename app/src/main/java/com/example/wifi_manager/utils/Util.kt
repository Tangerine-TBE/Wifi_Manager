package com.example.wifi_manager.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.example.module_base.utils.Constants
import com.example.module_base.utils.PackageUtil
import com.tamsiree.rxkit.view.RxToast

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


inline fun <reified T>toOtherResultActivity(context: Activity?,requestCode:Int,block:Intent.()->Unit){
    val intent = Intent(context,T::class.java)
    intent.block()
    context?.startActivityForResult(intent,requestCode)
}


fun copyContent(context: Context,result:String){
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val mClipData = ClipData.newPlainText("Label", result)
    cm.setPrimaryClip(mClipData)
    RxToast.normal("已经复制到剪切板")
}


fun shareContent(context: Context,result:String){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain" // 纯文本
        putExtra(Intent.EXTRA_SUBJECT, PackageUtil.getAppMetaData(context, Constants.APP_NAME))
        putExtra(Intent.EXTRA_TEXT, result)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(Intent.createChooser(intent,PackageUtil.getAppMetaData(context, Constants.APP_NAME)))
}