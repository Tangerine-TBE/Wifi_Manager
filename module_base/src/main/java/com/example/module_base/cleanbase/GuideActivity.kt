package com.example.module_base.cleanbase

import android.view.MotionEvent
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.module_base.R

import kotlinx.android.synthetic.main.act_guide_clean.*

class GuideActivity: BaseActivity() {
    override fun isStatusBarDarkFont(): Boolean {
        return true
    }

    override fun getStatusBarColor(): Int {
        return android.R.color.transparent
    }

    override fun getLayoutId()= R.layout.act_guide_clean

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.ROTATION_ANIMATION_CHANGED)
        Glide.with(this).load(R.drawable.guide).into(imageView)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        finish()
        return super.onTouchEvent(event)
    }
}