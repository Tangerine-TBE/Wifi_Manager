package com.example.module_base.utils

import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity

/**
 * @author: 铭少
 * @date: 2021/1/16 0016
 * @description：
 */


//不全屏
inline fun <reified T: View>setStatusBar(activity: FragmentActivity?, view: T, layoutType: LayoutType){
    val layoutParams = when (layoutType) {
        LayoutType.RELATIVELAYOUT -> view.layoutParams as RelativeLayout.LayoutParams
        LayoutType.LINEARLAYOUT-> view.layoutParams as LinearLayout.LayoutParams
        LayoutType.CONSTRAINTLAYOUT -> view.layoutParams as ConstraintLayout.LayoutParams
        else ->view.layoutParams as RelativeLayout.LayoutParams
    }
    layoutParams.topMargin= MyStatusBarUtil.getStatusBarHeight(activity)
    view.layoutParams=layoutParams
}


//获取当前线程名字
fun getCurrentThreadName()=Thread.currentThread().name

