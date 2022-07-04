package com.feisukj.cleaning.ui.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.feisukj.cleaning.R
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_open_android_r_permission.*

class OpenAndroidRPermissionActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_android_r_permission)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .statusBarDarkFont(true)
                .init()
        window.addFlags(WindowManager.LayoutParams.ROTATION_ANIMATION_CHANGED)
        Glide.with(this).load(R.drawable.open_android_r_permission).into(openFinger)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        finish()
        return super.onTouchEvent(event)
    }
}