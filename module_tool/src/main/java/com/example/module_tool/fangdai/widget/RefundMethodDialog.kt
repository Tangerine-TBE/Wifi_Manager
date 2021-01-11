package com.feisukj.main.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.module_tool.R
import com.example.module_tool.utils.DeviceUtils
import com.feisukj.main.bean.RefundMethod

import kotlinx.android.synthetic.main.layout_refund_method.*

/**
 * Created by Gpp on 2018/6/12.
 */
class RefundMethodDialog(context: Context, showText: String, listener: RefundMethodSelectedListener) : Dialog(context, R.style.BottomDialog) {

    var refundMethod: RefundMethod = RefundMethod.INTEREST

    init {
        //-----初始化dialog--start--
        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_refund_method, null)
        setContentView(contentView)
        val params = contentView.layoutParams as ViewGroup.MarginLayoutParams
//        params.width = context.resources.displayMetrics.widthPixels - DensityUtil.dp2px(16f)
//        params.bottomMargin = DensityUtil.dp2px(20f)
        params.width = context.resources.displayMetrics.widthPixels
        params.bottomMargin = DeviceUtils.dp2px(context,0f)
        contentView.layoutParams = params
        val window = window
        window?.setGravity(Gravity.BOTTOM)
        setCanceledOnTouchOutside(true)
        interest.isChecked = showText == context.resources.getString(R.string.equivalent)
        capital.isChecked = showText == context.resources.getString(R.string.principal)
        getWindow()!!.setWindowAnimations(R.style.BottomDialog_Animation)
        //-----初始化dialog--end--


        //-----初始化view--start--
        interest.setOnClickListener(null)
        capital.setOnClickListener(null)

        interest.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                refundMethod = RefundMethod.INTEREST
            }
        }
        capital.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                refundMethod = RefundMethod.CAPITAL
            }
        }

        mCancel.setOnClickListener { dismiss() }
        confirm.setOnClickListener {
            listener.refundMethod(refundMethod)
            dismiss()
        }
        //-----初始化view--end--

    }

}
