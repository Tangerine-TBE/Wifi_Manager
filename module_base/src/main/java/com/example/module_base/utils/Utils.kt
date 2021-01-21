package com.example.module_base.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BaseApplication
import java.lang.Exception


/**
 * @author: 铭少
 * @date: 2021/1/16 0016
 * @description：
 */


//不全屏
inline fun <reified T : View>setStatusBar(activity: FragmentActivity?, view: T, layoutType: LayoutType){
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
fun getCurrentThreadName()=Thread.currentThread().name


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