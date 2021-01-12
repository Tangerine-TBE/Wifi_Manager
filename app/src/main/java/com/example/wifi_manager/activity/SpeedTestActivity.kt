package com.example.wifi_manager.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_base.base.BaseActivity
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.widget.MyToolbar
import com.example.wifi_manager.R
import com.example.wifi_manager.utils.setToolBar
import kotlinx.android.synthetic.main.activity_speed_test.*

class SpeedTestActivity : BaseActivity() {
    override fun getLayoutView(): Int=R.layout.activity_speed_test

    override fun initView() {
        setToolBar(this,"网络测速",mSpeedToolbar)
    }

    override fun initEvent() {
        mSpeedToolbar.setOnBackClickListener(object:MyToolbar.OnBackClickListener{
            override fun onBack() {
                finish()
            }
            override fun onRightTo() {
            }
        })
    }

}