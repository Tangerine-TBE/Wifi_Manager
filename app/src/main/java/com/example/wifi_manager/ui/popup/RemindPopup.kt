package com.example.wifi_manager.ui.popup

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BasePopup
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.PopupRemindWindowBinding


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/19 11:05:14
 * @class describe
 */
class RemindPopup(activity: FragmentActivity?):
    BasePopup<PopupRemindWindowBinding>(activity, R.layout.popup_remind_window,ViewGroup.LayoutParams.MATCH_PARENT) {

    override fun initEvent() {
        mView.apply {
            cancel.setOnClickListener {
                this@RemindPopup.dismiss()
            }

            sure.setOnClickListener {
                this@RemindPopup.dismiss()
                mListener?.sure()
            }

        }
    }

    fun setRemindContent(content:String){
        mView.tip.text=content
    }

}