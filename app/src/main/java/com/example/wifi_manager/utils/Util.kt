package com.example.wifi_manager.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import com.example.module_base.utils.Constants
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.PackageUtil
import com.example.module_base.widget.MyToolbar
import com.tamsiree.rxkit.view.RxToast
import java.util.*

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
    val mClipData = ClipData.newPlainText("text", result)
    cm.setPrimaryClip(mClipData)
    RxToast.normal("已复制到剪切板")
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


 fun setToolBar(activity: Activity,title:String,view: MyToolbar) {
    MyStatusBarUtil.setColor(activity, Color.WHITE)
    view.setTitle(title)
}



fun calLastedTime(endDate: Date, nowDate: Date): Long {
    val nd = 1000 * 24 * 60 * 60.toLong()
    val nh = 1000 * 60 * 60.toLong()
    val nm = 1000 * 60.toLong()
    val ns = 1000;
    // 获得两个时间的毫秒时间差异
    val diff = endDate.time - nowDate.time
    // 计算差多少天
    val day = diff / nd
    // 计算差多少小时
    val hour = diff % nd / nh
    // 计算差多少分钟
    val min = diff % nd % nh / nm
    // 计算差多少秒//输出结果
    val sec = diff % nd % nh % nm / ns;
    return sec
}