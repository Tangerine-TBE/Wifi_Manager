package com.example.wifi_manager.ui.popup


import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.PopupRenameWindowBinding
import com.tamsiree.rxkit.RxKeyboardTool





/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/12 18:20:49
 * @class describe
 */
class RenamePopup(activity: FragmentActivity):BasePopup<PopupRenameWindowBinding>(activity, R.layout.popup_rename_window){

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