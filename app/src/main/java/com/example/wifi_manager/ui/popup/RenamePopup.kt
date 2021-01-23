package com.example.wifi_manager.ui.popup

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.wifi_manager.R
import com.tamsiree.rxkit.RxKeyboardTool
import kotlinx.android.synthetic.main.popup_mac_help_window.view.*
import kotlinx.android.synthetic.main.popup_mac_help_window.view.mIKnow
import kotlinx.android.synthetic.main.popup_rename_window.view.*
import kotlinx.android.synthetic.main.popup_rename_window.view.cancel

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/12 18:20:49
 * @class describe
 */
class RenamePopup(activity: FragmentActivity):BasePopup(activity, R.layout.popup_rename_window){

    override fun initEvent() {
        mView?.apply {
            cancel.setOnClickListener {
                this@RenamePopup.dismiss()
            }


            sure.setOnClickListener {
                mListener?.sure()
                this@RenamePopup.dismiss()
            }
        }
    }

    fun setOldName(name:String){
        mView.renameInput.setText(name)
    }

    fun  getRenameText()=mView.renameInput.text.trim().toString()

    override fun showPopupView(view: View, gravity: Int, x: Int, y: Int) {
        super.showPopupView(view, gravity, x, y)
        activity?.let {
            mView.renameInput.apply {
                setSelection(text.length)
                RxKeyboardTool.toggleSoftInput(it,this)
            }
        }
    }
}