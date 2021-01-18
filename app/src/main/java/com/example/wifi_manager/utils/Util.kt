package com.example.wifi_manager.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import com.example.module_base.utils.Constants
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.PackageUtil
import com.example.module_base.widget.MyToolbar
import com.tamsiree.rxkit.RxNetTool
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


//跳转Activity
inline fun <reified T>toOtherActivity(activity: Activity?,block:Intent.()->Unit){
    val intent = Intent(activity,T::class.java)
    intent.block()
    activity?.startActivity(intent)
}

//跳转Activity
inline fun <reified T>toOtherActivity(activity: Activity?,isFinish: Boolean,block:Intent.()->Unit){
    val intent = Intent(activity,T::class.java)
    intent.block()
    activity?.startActivity(intent)
    if (isFinish) {
        activity?.finish()
    }
}

//跳转Activity带请求码
inline fun <reified T>toOtherResultActivity(context: Activity?,requestCode:Int,block:Intent.()->Unit){
    val intent = Intent(context,T::class.java)
    intent.block()
    context?.startActivityForResult(intent,requestCode)
}
//复制
fun copyContent(context: Context,result:String){
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val mClipData = ClipData.newPlainText("text", result)
    cm.setPrimaryClip(mClipData)
    RxToast.normal("已复制到剪切板")
}
//分享
fun shareContent(context: Context,result:String){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain" // 纯文本
        putExtra(Intent.EXTRA_SUBJECT, PackageUtil.getAppMetaData(context, Constants.APP_NAME))
        putExtra(Intent.EXTRA_TEXT, result)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(Intent.createChooser(intent,PackageUtil.getAppMetaData(context, Constants.APP_NAME)))
}
//设置页面和状态栏的距离
 fun setToolBar(activity: Activity,title:String,view: MyToolbar,color: Int=Color.WHITE) {
    MyStatusBarUtil.setColor(activity,color)
    view.setTitle(title)
}

//计算相差几秒
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
//toolbar事件
fun MyToolbar.toolbarEvent(activity: Activity,event:()->Unit){
    setOnBackClickListener(object :MyToolbar.OnBackClickListener{
        override fun onBack() {
            activity?.finish()
        }
        override fun onRightTo() {
            event()
        }
    })
}

//计时
fun startCountDown(totalTime: Long, followTime: Long, finish: () -> Unit, ticking: () -> Unit) = object:CountDownTimer(totalTime,followTime){
        override fun onFinish() {
            finish()
        }
        override fun onTick(millisUntilFinished: Long) {
            ticking()
        }
    }


//Wifi是否连接
fun isWifiConnected(context: Context)=RxNetTool.isWifiConnected(context)

//网络是否连接
fun isConnectedWifi(context: Context)=RxNetTool.isConnected(context)


//弹出toast
fun showToast(str:String){
    RxToast.normal(str)
}


