package com.example.wifi_manager.ui.popup

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.wifi_manager.R
import kotlinx.android.synthetic.main.popup_mac_help_window.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/12 18:20:49
 * @class describe
 */
class CheckMacHelpPopup(activity: Activity):BasePopup(activity, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT){
    private val mView= LayoutInflater.from(activity).inflate(R.layout.popup_mac_help_window,null)
    init {
        contentView = mView
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isFocusable = true
        isOutsideTouchable = false
        animationStyle= R.style.popupAnimation
        initEvent()
    }

    private fun initEvent() {
        mView?.apply {
            mIKnow.setOnClickListener {
                this@CheckMacHelpPopup.dismiss()
            }
        }


    }
}