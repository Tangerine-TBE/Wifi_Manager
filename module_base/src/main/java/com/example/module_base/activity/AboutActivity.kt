package com.example.module_base.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_base.R
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.databinding.ActivityAboutBinding
import com.example.module_base.utils.PackageUtil
import com.example.module_base.utils.setToolBar
import com.example.module_base.utils.toolbarEvent

class AboutActivity : BaseViewActivity<ActivityAboutBinding>() {

    override fun getLayoutView(): Int=R.layout.activity_about


    override fun initView() {

        binding.apply {
            setToolBar(this@AboutActivity,"关于我们",aboutToolbar)
            version.text="${PackageUtil.packageCode(this@AboutActivity)}"
        }

    }

    override fun initEvent() {
        binding.aboutToolbar.toolbarEvent(this) {}
    }

}