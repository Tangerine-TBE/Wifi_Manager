package com.example.wifi_manager.ui.popup


import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BasePopup
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.PopupMacHelpWindowBinding


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/12 18:20:49
 * @class describe
 */
class CheckMacHelpPopup(activity: FragmentActivity):
    BasePopup<PopupMacHelpWindowBinding>(activity, R.layout.popup_mac_help_window){
    init {
        animationStyle= R.style.popupAnimation
    }
    override fun initEvent() {
        mView?.apply {
            mIKnow.setOnClickListener {
                this@CheckMacHelpPopup.dismiss()
            }
        }
    }
}