package com.example.wifi_manager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.toolbarEvent
import com.example.wifi_manager.R
import com.example.wifi_manager.databinding.ActivityProtectNetBinding

class ProtectNetActivity : BaseViewActivity<ActivityProtectNetBinding>() {

    override fun getLayoutView(): Int = R.layout.activity_protect_net

    override fun initView() {
        binding.apply {
            setToolBar(this@ProtectNetActivity, "如何防蹭网", protectNetToolbar)

        }
    }

    override fun initEvent() {
        binding.protectNetToolbar.toolbarEvent(this) {}
    }

}