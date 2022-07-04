package com.feisukj.base.widget.Rx

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.example.module_base.R
import com.example.module_base.utils.ToastUtil
import com.tamsiree.rxui.view.dialog.shapeloadingview.RxShapeLoadingView

/**
 * @author Tamsiree
 * @date 2017/3/16
 */
class RxDialogShapeLoading : RxDialog {
    lateinit var loadingView: RxShapeLoadingView
        private set
    lateinit var dialogContentView: View
        private set

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        initView(context)
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
        initView(context)
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Activity) : super(context) {
        initView(context)
    }

    constructor(context: Context, alpha: Float, gravity: Int) : super(context, alpha, gravity) {
        initView(context)
    }

    private fun initView(context: Context) {
        dialogContentView = LayoutInflater.from(context).inflate(R.layout.dialog_shape_loading_view, null)
        loadingView = dialogContentView.findViewById(R.id.loadView)
        setContentView(dialogContentView)
    }

    fun cancel(code: RxCancelType?, str: String?) {
        cancel()
//        when (code) {
//            RxCancelType.normal -> normal(str!!)
//            RxCancelType.error -> error(str!!)
//            RxCancelType.success -> success(str!!)
//            RxCancelType.info -> info(str!!)
//            else -> normal(str!!)
//        }
        ToastUtil.showToast(str!!)
    }

    fun cancel(str: String?) {
        cancel()
//        normal(str!!)
        ToastUtil.showToast(str!!)
    }

    fun setLoadingText(charSequence: CharSequence?) {
        loadingView.setLoadingText(charSequence)
    }

    enum class RxCancelType {
        normal, error, success, info
    }
}