package com.example.wifi_manager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.toolbarEvent
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivitySafetyBinding
import com.example.wifi_manager.viewmodel.SafetyCheckViewModel

class SafetyCheckActivity : BaseVmViewActivity<ActivitySafetyBinding,SafetyCheckViewModel>() {
    override fun getViewModelClass(): Class<SafetyCheckViewModel> {
        return SafetyCheckViewModel::class.java
    }
    override fun getLayoutView(): Int=R.layout.activity_safety


    override fun initView() {
        setToolBar(this,"安全检测",binding.safetyCheckToolbar)
        binding.protectCheckView.startProtectCheck()
    }

    override fun initEvent() {
        binding.apply {
            safetyCheckToolbar.toolbarEvent(this@SafetyCheckActivity){}



        }
    }

}