package com.feisu.module_battery.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.example.module_base.cleanbase.BaseFragment2
import com.example.module_base.utils.LogUtils
import com.feisu.module_battery.R
import com.feisu.module_battery.bean.BatteryInfo


import kotlinx.android.synthetic.main.fragment_charge.*

class ChargeFragment: BaseFragment2() {
    override fun getLayoutId()= R.layout.fragment_charge

    override fun initView() {
        for (i in 0 until chargeStatic.childCount){
            chargeStatic.getChildAt(i).setOnTouchListener { view, _ ->
                view.performClick()
                return@setOnTouchListener true
            }
        }

        LogUtils.i("-----------ChargeFragment------------------")
    }

    override fun initListener() {
        batteryAbout.setOnClickListener {
            val popupWindow=PopupWindow(context)
            popupWindow.contentView=LayoutInflater.from(context).inflate(
                R.layout.window_battery_about,
                null
            )
            popupWindow.width=resources.displayMetrics.widthPixels/2
            popupWindow.isOutsideTouchable=true
            popupWindow.setBackgroundDrawable(null)
            popupWindow.showAsDropDown(it, -popupWindow.width, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            val intentFilter= IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            it.registerReceiver(batteryInfoReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            it.unregisterReceiver(batteryInfoReceiver)
        }
    }

    private val batteryInfoReceiver=object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action!= Intent.ACTION_BATTERY_CHANGED){
                return
            }
            val batteryInfo= BatteryInfo(intent)
            updateInfo(batteryInfo)
        }
    }

    private fun updateInfo(batteryInfo: BatteryInfo){
        ((batteryInfo.br*100+0.5).toInt().toString()+"%").also {
            currentElectricQuantityValue.text= it
            currentElectricQuantityValue2.text= it
        }
        val time=batteryInfo.batteryFullNeedTime()
        if (time==null){
            if (chargeViewSwitcher.currentView!=notCharge){
                chargeViewSwitcher.showNext()
            }
            notCharge.text=""
            return
        }
        batteryStatePictures.batterQuantity=batteryInfo.br
        when {
            batteryInfo.br<0.8 -> {
                fastCharge.isChecked=true
            }
            batteryInfo.br<0.95 -> {
                turbineCharge.isChecked=true
            }
            else -> {
                cyclicCharge.isChecked=true
            }
        }
        if (batteryInfo.static==BatteryManager.BATTERY_STATUS_CHARGING){
            if (chargeViewSwitcher.currentView!=charge){
                chargeViewSwitcher.showNext()
            }
            remainingTimeValueHour.text=(time/60).toString()
            remainingTimeValueMinute.text=(time%60).toString()
            batteryStatePictures.startChargeAnimation()
        }else{
            if (chargeViewSwitcher.currentView!=notCharge){
                chargeViewSwitcher.showNext()
            }
            notCharge.text=batteryInfo.statusString
            batteryStatePictures.stopChargeAnimation()

            chargeStatic.clearCheck()
        }
    }
}