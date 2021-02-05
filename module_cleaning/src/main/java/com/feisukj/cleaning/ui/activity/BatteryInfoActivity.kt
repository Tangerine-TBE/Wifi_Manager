package com.feisukj.cleaning.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle

import androidx.fragment.app.FragmentActivity
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.BanFeedHelper
import com.example.module_ad.advertisement.InsertHelper
import com.example.module_base.utils.SPUtil

import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.BatteryInfo
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_battery_info.*
import java.util.*

class BatteryInfoActivity : FragmentActivity(){
    companion object{
        const val SAVE_ELECTRICITY_TIME_KEY="SAVE_ELECTRICITY_TIME_KEY"
        const val INTERVAL_TIME=5
    }

    private var batteryAnimator:Animator?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_info)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()

        saveElectricity.setOnClickListener {
            val interval=(System.currentTimeMillis()- SPUtil.getInstance().getLong(SAVE_ELECTRICITY_TIME_KEY))/1000/60
            if (interval>= INTERVAL_TIME){
                startActivity(Intent(this, OptimizeActivity::class.java))
                SPUtil.getInstance().putLong(SAVE_ELECTRICITY_TIME_KEY,System.currentTimeMillis())
            }else{
                startActivity(Intent(this, OptimizeCompleteActivity::class.java))
            }
        }

        backOne.setOnClickListener {
            finish()
        }


        banFeedHelper.showAd(AdType.BATTERY_PAGE)

    }


    private val banFeedHelper by lazy {
        BanFeedHelper(this, frameAd, frameLayout)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter= IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryInfoReceiver,intentFilter)
        if (batteryAnimator?.isPaused==true){
            batteryAnimator?.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryInfoReceiver)
        if (batteryAnimator?.isRunning==true){
            batteryAnimator?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (batteryAnimator?.isStarted==true){
            batteryAnimator?.cancel()
        }

        banFeedHelper.releaseAd()
    }

    private val batteryInfoReceiver=object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action!=Intent.ACTION_BATTERY_CHANGED){
                return
            }
            val batteryInfo= BatteryInfo(intent)
            updateInfo(batteryInfo)
        }
    }

    private fun updateInfo(batteryInfo: BatteryInfo){
        progressBattery.text=(batteryInfo.br*100+0.5).toInt().toString()+"%"
        batteryStatusValue.text=batteryInfo.statusString
        batteryVoltageValue.text=batteryInfo.voltage
        batteryTechnologyValue.text=batteryInfo.technology?:"未知"
        val random=Random()
        batteryScoring.text=when(batteryInfo.healthCode){
            BatteryManager.BATTERY_HEALTH_GOOD ->{
                "${90+random.nextInt(7)}分"
            }
            BatteryManager.BATTERY_HEALTH_OVERHEAT ->{
                "${60+random.nextInt(7)}分"
            }
            BatteryManager.BATTERY_HEALTH_COLD ->{
                "${70+random.nextInt(7)}分"
            }
            BatteryManager.BATTERY_HEALTH_DEAD ->{
                "${80+random.nextInt(7)}分"
            }
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE ->{
                "${70+random.nextInt(7)}分"
            }
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE ->{
                "${50+random.nextInt(7)}分"
            }
            BatteryManager.BATTERY_HEALTH_UNKNOWN ->{
                "${50+random.nextInt(7)}分"
            }
            else->{
                "${50+random.nextInt(7)}分"
            }
        }

        batteryInfo.getCanUseTime().also {
            if (it==null){
                availableTime.text="获取可使用时间失败"

                callUseTimeValue.text="--"
                videoUseTimeValue.text="--"
                shortVideoUseTimeValue.text="--"
            }else{
                val useTime={minute:Int->
                    val h=minute/60
                    val m=minute%60
                    "${h}小时${m}分钟"
                }

                availableTime.text=useTime(it)
//                2115
//                738//通话
//                928//影音
//                1043//短视频
                callUseTimeValue.text=useTime((it*0.35f+0.5f).toInt())
                videoUseTimeValue.text=useTime((it*0.44f+0.5f).toInt())
                shortVideoUseTimeValue.text=useTime((it*0.494f+0.5f).toInt())
            }
        }

        if (batteryInfo.static==BatteryManager.BATTERY_STATUS_CHARGING){
            if (batteryAnimator==null){
                batteryAnimator=ValueAnimator.ofFloat(batteryInfo.br,1f).also { animator ->
                    animator.addUpdateListener { valueAnimator ->
                        (valueAnimator.animatedValue as? Float)?.let {
                            batteryView.batterQuantity=it
                            batteryView.invalidate()
                        }
                    }
                    animator.duration=3000
                    animator.repeatCount=ValueAnimator.INFINITE
                    animator.repeatMode=ValueAnimator.RESTART
                }
                batteryAnimator?.start()
            }else{
                batteryAnimator?.start()
            }
        }else{
            batteryAnimator?.cancel()
            batteryView.batterQuantity=batteryInfo.br
        }
    }


   // onDestroy

}