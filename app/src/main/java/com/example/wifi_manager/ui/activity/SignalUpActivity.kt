package com.example.wifi_manager.ui.activity

import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.setStatusBar
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySignalUpBinding
import com.example.wifi_manager.utils.toOtherActivity
import com.example.wifi_manager.utils.toolbarEvent


class SignalUpActivity : BaseViewActivity<ActivitySignalUpBinding>() {
    override fun getLayoutView(): Int=R.layout.activity_signal_up

    override fun initView() {
        binding.apply {
            setStatusBar(this@SignalUpActivity, binding.mSignalUpToolbar, LayoutType.LINEARLAYOUT)
        }
    }


    override fun initEvent() {
    binding.apply {
        mSignalUpToolbar.toolbarEvent(this@SignalUpActivity){}

        signalNormalLayout.mOptimizeHardwareBt.setOnClickListener {
            toOtherActivity<HardwareTweaksActivity>(this@SignalUpActivity){}
        }

        nSignalUp.setOnClickListener {

        }
    }
    }
}