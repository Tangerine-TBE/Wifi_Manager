package com.example.wifi_manager.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleObserver

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.base
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/11 11:10:37
 * @class describe
 */
open class BaseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),LifecycleObserver {
}