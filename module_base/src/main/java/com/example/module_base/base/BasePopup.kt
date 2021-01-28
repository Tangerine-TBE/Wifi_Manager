package com.example.module_base.base

import android.animation.ValueAnimator

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity


/**
 * @name AlarmClock
 * @class name：com.example.alarmclock.ui.weight
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2020/11/26 17:21
 * @class describe
 */
open class BasePopup<T:ViewDataBinding>(val activity: FragmentActivity?, layout:Int, width:Int= ViewGroup.LayoutParams.WRAP_CONTENT, height:Int=ViewGroup.LayoutParams.WRAP_CONTENT): PopupWindow(width, height) {

    protected val mView = DataBindingUtil.inflate<T>(LayoutInflater.from(activity),layout,null,false)
    init {
        contentView = mView.root
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isFocusable = true
        isOutsideTouchable = false
        intBgAnimation()
        setOnDismissListener {
            mOutValueAnimator?.start()

        }
        initEvent()
    }

    open fun initEvent() {
    }

    //设置窗口渐变
    private fun updateBgWindowAlpha(alpha: Float) {
        val window = activity?.window
        val attributes = window?.attributes
        attributes?.alpha = alpha
        window?.attributes = attributes
    }


    lateinit var mInValueAnimator: ValueAnimator
    lateinit var mOutValueAnimator: ValueAnimator

    private fun intBgAnimation() {
        mInValueAnimator = ValueAnimator.ofFloat(1.0f, 0.5f)
        mInValueAnimator?.duration = 300
        mInValueAnimator?.addUpdateListener { animation -> updateBgWindowAlpha(animation.animatedValue as Float) }
        mOutValueAnimator = ValueAnimator.ofFloat(0.5f, 1.0f)
        mOutValueAnimator?.duration = 100
        mOutValueAnimator?.addUpdateListener { animation -> updateBgWindowAlpha(animation.animatedValue as Float) }
    }


   open fun showPopupView(view: View,gravity:Int=Gravity.CENTER,x:Int=0,y:Int=0){
       activity?.let {
           if (!it.isFinishing) {
               mInValueAnimator.start()
               showAtLocation(view,gravity,x,y)
           }
       }
    }

    interface OnActionClickListener{
        fun sure()

        fun cancel()
    }

    protected var mListener:OnActionClickListener?=null
    fun setOnActionClickListener(listener:OnActionClickListener){
        mListener=listener
    }



}