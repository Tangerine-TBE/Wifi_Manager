package com.example.wifi_manager.ui.popup

import android.app.Service
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.example.wifi_manager.R
import com.tamsiree.rxkit.RxKeyboardTool
import kotlinx.android.synthetic.main.popup_wifi_connect_window.view.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/18 10:29:30
 * @class describe
 */
class WifiConnectPopup(activity: FragmentActivity?):BasePopup(activity, R.layout.popup_wifi_connect_window, ViewGroup.LayoutParams.MATCH_PARENT) {
    private var isShowPwd=true

    override fun initEvent() {
        mView.apply {
            mView.sharePublicWifi.isChecked = true

            cancel.setOnClickListener {
                this@WifiConnectPopup.dismiss()
            }

            setOnDismissListener {
                wifiPwd.setText("")
                mOutValueAnimator?.start()
            }

            sure.setOnClickListener {
                mListener?.sure()
            }

            showPwd.setOnClickListener {
                wifiPwd.transformationMethod =if (isShowPwd) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
                isShowPwd=!isShowPwd
            }

        }
    }

    override fun showPopupView(view: View, gravity: Int, x: Int, y: Int) {
        super.showPopupView(view, gravity, x, y)
        activity?.let {
            RxKeyboardTool.toggleSoftInput(it,mView.wifiPwd)
        }
    }


    fun setWifiName(name: String){
        mView.connectWifiName.text=name+""
    }
    fun  getWifiPwd() =mView.wifiPwd.text.toString().trim()
    fun getShareState()=mView.sharePublicWifi.isChecked



}