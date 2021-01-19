package com.example.wifi_manager.ui.popup

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.wifi_manager.R
import kotlinx.android.synthetic.main.popup_remind_window.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/19 11:05:14
 * @class describe
 */
class RemindPopup(activity: FragmentActivity?):BasePopup(activity, R.layout.popup_remind_window,ViewGroup.LayoutParams.MATCH_PARENT) {

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

}