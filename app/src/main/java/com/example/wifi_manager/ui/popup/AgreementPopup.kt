package com.example.wifi_manager.ui.popup

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.activity.DealViewActivity
import com.example.module_base.base.BasePopup
import com.example.module_base.utils.Constants
import com.example.module_base.utils.PackageUtil
import com.example.module_base.utils.showToast
import com.example.module_base.utils.toOtherActivity
import com.example.module_tool.utils.ColorUtil
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.PopupAgreementWindowBinding
import com.example.wifi_manager.ui.adapter.recycleview.PermissionAdapter
import com.example.wifi_manager.utils.DataProvider
import com.tamsiree.rxkit.view.RxToast

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/28 13:42:27
 * @class describe
 */
class AgreementPopup(activity: FragmentActivity):BasePopup<PopupAgreementWindowBinding>(activity, R.layout.popup_agreement_window,ViewGroup.LayoutParams.MATCH_PARENT) {

    private val mPermissionAdapter by lazy {
        PermissionAdapter()
    }

    init {
        isFocusable=false
        isOutsideTouchable =false

        mView.apply {
            welcomeTitle.text="欢迎使用${PackageUtil.getAppMetaData(activity,Constants.APP_NAME)}"
            permissionContainer.layoutManager = LinearLayoutManager(activity)
            permissionContainer.adapter = mPermissionAdapter
            mPermissionAdapter.setList(DataProvider.permissionList)


            val str = activity.resources.getString(R.string.user_agreement)
            val stringBuilder = SpannableStringBuilder(str)
            val span1 = TextViewSpan1()
            stringBuilder.setSpan(span1, 10, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            val span2 = TextViewSpan2()
            stringBuilder.setSpan(span2, 19, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            descriptions.text=stringBuilder
            descriptions.movementMethod= LinkMovementMethod.getInstance()
        }




    }

    override fun initEvent() {
        mView.apply {
            ivCancel.setOnClickListener {
                dismiss()
                mListener?.cancel()

            }

            btSure.setOnClickListener {
                if (scbAgreement.isChecked) {
                    dismiss()
                    mListener?.sure()
                } else {
                    RxToast.warning("请确保您已同意本应用的隐私政策和用户协议")
                }
            }

        }

    }

    inner class TextViewSpan1 : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = ColorUtil.THEME_COLOR
        }

        override fun onClick(widget: View) {
            //点击事件
            toOtherActivity<DealViewActivity>(activity,false){
                putExtra(Constants.SET_DEAL1,1)
            }
        }
    }

    inner  class TextViewSpan2 : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = ColorUtil.THEME_COLOR
        }

        override fun onClick(widget: View) {
            //点击事件
            toOtherActivity<DealViewActivity>(activity,false){
             putExtra(Constants.SET_DEAL1, 2)
            }
        }
    }

}