package com.example.wifi_manager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.setStatusBar
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityHardwareTweaksBinding
import com.example.wifi_manager.utils.setToolBar
import com.example.wifi_manager.utils.toolbarEvent

class HardwareTweaksActivity : BaseViewActivity<ActivityHardwareTweaksBinding>() {
    override fun getLayoutView(): Int =R.layout.activity_hardware_tweaks
    override fun initView() {
        setStatusBar(this,binding.hardwareToolbar, LayoutType.CONSTRAINTLAYOUT)
    }

    override fun initEvent() {
        binding.hardwareToolbar.toolbarEvent(this) {}
    }

}