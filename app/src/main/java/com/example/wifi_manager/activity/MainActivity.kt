package com.example.wifi_manager.activity

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.module_base.base.BaseActivity
import com.example.wifi_manager.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getLayoutView(): Int = R.layout.activity_main

    override fun initView() {
        bottomNavigationView.setupWithNavController(findNavController(R.id.nav_home_fragment))
    }

}