package com.feisu.module_battery.ui.activity

import android.content.Intent
import com.example.module_base.cleanbase.BaseActivity2
import com.feisu.module_battery.R
import com.feisu.module_battery.bean.BatteryInfo

import kotlinx.android.synthetic.main.activity_save_electricity.*

class SaveElectricityActivity: BaseActivity2() {

    override fun getLayoutId()= R.layout.activity_save_electricity

    override fun initView() {
        setContentText("省电成功")
        val intent= Intent(Intent.ACTION_BATTERY_CHANGED)
        BatteryInfo(intent).also {
            val time=it.getCanUseTime()?:return@also
            saveSuccessHour.text=(time/60).toString()
            saveSuccessMinute.text=(time%60).toString()
        }
//        AdController.Builder(this,ADConstants.SAVE_ELECTRICITY_FINISHED)
//                .setContainer(frameLayout)
//                .create()
//                .show()
    }

    override fun initListener() {

    }
}