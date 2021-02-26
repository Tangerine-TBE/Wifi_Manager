package com.example.wifi_manager.ui.popup


import android.graphics.Rect
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_base.activity.DealViewActivity
import com.example.module_base.base.BasePopup
import com.example.module_base.utils.Constants
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.toOtherActivity
import com.example.module_tool.utils.ColorUtil
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.PopupWifiConnectWindowBinding
import com.tamsiree.rxkit.RxKeyboardTool
import java.lang.Exception


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/18 10:29:30
 * @class describe
 */
class WifiConnectPopup(activity: FragmentActivity?):
    BasePopup<PopupWifiConnectWindowBinding>(activity, R.layout.popup_wifi_connect_window, ViewGroup.LayoutParams.MATCH_PARENT) {
    private var isShowPwd=true


    init {
        val str ="分享为公共WiFi/已阅读并同意《功能说明》"
        val stringBuilder = SpannableStringBuilder(str)
        val span1 = TextViewSpan1()
        stringBuilder.setSpan(span1, str.length-5, str.length-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mView.shareDes.text=stringBuilder
        mView.shareDes.movementMethod= LinkMovementMethod.getInstance()
    }

    inner class TextViewSpan1 : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = ColorUtil.THEME_COLOR
        }

        override fun onClick(widget: View) {
            //点击事件
            RxKeyboardTool.hideSoftInput(mView.wifiPwd)
            toOtherActivity<DealViewActivity>(activity,false){
                putExtra(Constants.SET_DEAL1,3)
            }
        }
    }

    override fun initEvent() {
        mView.apply {
            mView.sharePublicWifi.isChecked = true

            cancel.setOnClickListener {
                this@WifiConnectPopup.dismiss()

            }

            setOnDismissListener {
                wifiPwd.setText("")
                isShowPwd=true
                wifiPwd.transformationMethod = PasswordTransformationMethod.getInstance()
                showPwd.setImageResource(R.mipmap.icon_hide_pwd)
                mOutValueAnimator?.start()
            }

            sure.setOnClickListener {
                mListener?.sure()
                RxKeyboardTool.hideSoftInput(mView.wifiPwd)

            }

            showPwd.setOnClickListener {
                showPwd.setImageResource(if(isShowPwd) R.mipmap.icon_show_pwd else R.mipmap.icon_hide_pwd)
                wifiPwd.apply {
                    transformationMethod =if (isShowPwd) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
                    setSelection(text.length)
                    isShowPwd=!isShowPwd

                }

            }

        }
    }

    override fun showPopupView(view: View, gravity: Int, x: Int, y: Int) {
        super.showPopupView(view, gravity, x, y)
        try {
            activity?.let {
                RxKeyboardTool.toggleSoftInput(it,mView.wifiPwd)
            }
        }catch (e:Exception){
        }

    }


    fun setWifiName(name: String){
        mView.connectWifiName.text=name+""
    }
    fun  getWifiPwd() =mView.wifiPwd.text.toString().trim()
    fun getShareState()=mView.sharePublicWifi.isChecked



}