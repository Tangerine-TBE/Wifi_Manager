package com.feisu.module_battery.ui.activity

import com.example.module_base.cleanbase.BaseActivity2
import com.feisu.module_battery.R

import kotlinx.android.synthetic.main.activity_battery_tips.*

class BatteryTipsActivity: BaseActivity2() {
    override fun isActionBar()=false

    override fun getStatusBarColor()= android.R.color.transparent

    override fun getLayoutId()= R.layout.activity_battery_tips

    override fun initView() {
        
    }

    override fun initListener() {
        batteryTipBack.setOnClickListener { finish() }
    }
}