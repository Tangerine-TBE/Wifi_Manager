package com.example.module_base.utils

import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity

/**
 * @name AlarmClock
 * @class name：com.example.alarmclock.util
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2020/11/16 18:24
 * @class describe
 */
object  MarginStatusBarUtil {
    inline fun <reified T:View>setStatusBar(activity: FragmentActivity?, view: T,layoutType:Int){
        val layoutParams = when (layoutType) {
            0 -> view.layoutParams as RelativeLayout.LayoutParams
            1-> view.layoutParams as LinearLayout.LayoutParams
            2 -> view.layoutParams as ConstraintLayout.LayoutParams
            else ->view.layoutParams as RelativeLayout.LayoutParams
        }
        layoutParams.topMargin= MyStatusBarUtil.getStatusBarHeight(activity)
        view.layoutParams=layoutParams
    }
}