package com.example.module_base.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BaseApplication
import com.example.module_base.widget.LoadingDialog
import com.example.module_base.widget.MyToolbar
import com.google.gson.Gson
import com.permissionx.guolindev.PermissionX
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
inline fun <reified T>toOtherActivity(activity: Activity?, block: Intent.() -> Unit){
    val intent = Intent(activity, T::class.java)
    intent.block()
    activity?.startActivity(intent)
}

//跳转Activity
inline fun <reified T>toOtherActivity(
    activity: Activity?,
    isFinish: Boolean,
    block: Intent.() -> Unit
){
    val intent = Intent(activity, T::class.java)
    intent.block()
    activity?.startActivity(intent)
    if (isFinish) {
        activity?.finish()
    }
}

//跳转Activity带请求码
inline fun <reified T>toOtherResultActivity(
    context: Activity?,
    requestCode: Int,
    block: Intent.() -> Unit
){
    val intent = Intent(context, T::class.java)
    intent.block()
    context?.startActivityForResult(intent, requestCode)
}
//复制
fun copyContent(context: Context, result: String){
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val mClipData = ClipData.newPlainText("text", result)
    cm.setPrimaryClip(mClipData)
    RxToast.normal("已复制到剪切板")
}
//分享
fun shareContent(context: Context, result: String){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain" // 纯文本
        putExtra(Intent.EXTRA_SUBJECT, PackageUtil.getAppMetaData(context, Constants.APP_NAME))
        putExtra(Intent.EXTRA_TEXT, result)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(
        Intent.createChooser(
            intent, PackageUtil.getAppMetaData(
                context,
                Constants.APP_NAME
            )
        )
    )
}
//设置页面和状态栏的距离
 fun setToolBar(activity: Activity, title: String, view: MyToolbar, color: Int = Color.WHITE) {
    MyStatusBarUtil.setColor(activity, color)
    view.setTitle(title)
}

//计算相差时间
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
    return day+1
}
//toolbar事件
fun MyToolbar.toolbarEvent(activity: Activity, event: () -> Unit){
    setOnBackClickListener(object : MyToolbar.OnBackClickListener {
        override fun onBack() {
            activity?.finish()
        }

        override fun onRightTo() {
            event()
        }
    })
}

//计时
fun startCountDown(totalTime: Long, followTime: Long, finish: () -> Unit, ticking: () -> Unit) = object:CountDownTimer(
    totalTime,
    followTime
){
        override fun onFinish() {
            finish()
        }
        override fun onTick(millisUntilFinished: Long) {
            ticking()
        }
    }


//网络是否连接
fun isConnectedWifi(context: Context)=RxNetTool.isConnected(context)


//弹出toast
fun showToast(str: String){
    RxToast.normal(str)
}


//弹出Dialog
fun LoadingDialog.showDialog(activity: Activity?){
    activity?.let {
        if (!it.isFinishing) {
            show()
        }
    }
}

//不全屏
inline fun <reified T : View>setStatusBar(
    activity: FragmentActivity?,
    view: T,
    layoutType: LayoutType
){
    val layoutParams = when (layoutType) {
        LayoutType.RELATIVELAYOUT -> view.layoutParams as RelativeLayout.LayoutParams
        LayoutType.LINEARLAYOUT -> view.layoutParams as LinearLayout.LayoutParams
        LayoutType.CONSTRAINTLAYOUT -> view.layoutParams as ConstraintLayout.LayoutParams
        else ->view.layoutParams as RelativeLayout.LayoutParams
    }
    layoutParams.topMargin= MyStatusBarUtil.getStatusBarHeight(activity)
    view.layoutParams=layoutParams
}


//获取当前线程名字
fun getCurrentThreadName(): String =Thread.currentThread().name


fun toAppShop(activity: Activity?){
    activity?.let {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=${BaseApplication.mPackName}")
            it.startActivity(intent)
        } catch (e: Exception) {
            it.finish()
        }
    }
}


inline fun <reified T> gsonHelper(result: String?): T? =
        try {
            if (result!=null) {
                Gson().fromJson(result, T::class.java)
            } else {
                null
            }
        }catch (e: Exception){
            null
        }


fun checkAppPermission(
    permissions: ArrayList<String>,
    success: () -> Unit,
    fail: () -> Unit,
    activity: FragmentActivity? = null,
    fragment: Fragment? = null
){
    try {
        val permissionCollection = if (activity == null) {
            PermissionX.init(fragment)
        } else {
            PermissionX.init(activity)
        }
        permissionCollection
            .permissions(permissions)
            .setDialogTintColor(
                Color.parseColor("#285FF5"),
                Color.parseColor("#285FF5")
            )
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                val msg = "即将申请的权限是程序必须依赖的权限"
                scope.showRequestReasonDialog(deniedList, msg, "开启", "取消")
            }
            .onForwardToSettings { scope, deniedList ->
                val msg = "您需要去应用程序设置当中手动开启权限"
                scope.showForwardToSettingsDialog(deniedList, msg, "开启", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    success()
                } else {
                    fail()
                }
            }
    }catch (e: Exception){
    }

}



