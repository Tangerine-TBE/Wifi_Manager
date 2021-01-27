package com.feisu.module_battery.bean

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.BatteryManager.*
import com.example.module_base.cleanbase.BaseConstant

import java.lang.Exception
import java.text.NumberFormat

class BatteryInfo(intent: Intent) {
    init {
        if (intent.action!=Intent.ACTION_BATTERY_CHANGED){
            throw IllegalArgumentException("Intent的action必须是Intent.ACTION_BATTERY_CHANGED")
        }
    }
    companion object{
        val batteryCategory by lazy {
            val powerProfileClass = "com.android.internal.os.PowerProfile"
            try {
                val any=Class.forName(powerProfileClass)
                        .getConstructor(Context::class.java)
                        .newInstance(BaseConstant.application)
                Class.forName(powerProfileClass)
                        .getMethod("getBatteryCapacity")
                        .invoke(any) as? Double

            }catch (e:Exception){
                null
            }
        }
        fun getBatteryCapacityDes():String{
            return if (batteryCategory==null){
                "获取失败"
            }else{
                "$batteryCategory mAh"
            }
        }
    }

    val static=intent.getIntExtra(EXTRA_STATUS, BATTERY_STATUS_UNKNOWN )

    val statusString=
            when(static){
                BATTERY_STATUS_CHARGING ->{
                    "充电状态"
                }
                BATTERY_STATUS_DISCHARGING  ->{
                    "放电状态"
                }
                BATTERY_STATUS_FULL ->{
                    "满电状态"
                }
                BATTERY_STATUS_NOT_CHARGING ->{
                    "不充电状态"
                }
                BATTERY_STATUS_UNKNOWN ->{
                    "未知状态"
                }
                else->{
                    "未知状态"
                }
            }

    val health=
            when(intent.getIntExtra(EXTRA_HEALTH, BATTERY_HEALTH_UNKNOWN)){
                BATTERY_HEALTH_GOOD->{
                    "健康"
                }
                BATTERY_HEALTH_OVERHEAT->{
                    "过热"
                }
                BATTERY_HEALTH_COLD->{
                    "温度太低"
                }
                BATTERY_HEALTH_DEAD->{
                    "良好"
                }
                BATTERY_HEALTH_OVER_VOLTAGE->{
                    "电压过大"
                }
                BATTERY_HEALTH_UNSPECIFIED_FAILURE->{
                    "未明示故障"
                }
                BATTERY_HEALTH_UNKNOWN->{
                    "未知状态"
                }
                else->{
                    "未知状态"
                }
            }

//    val present=intent.getBooleanExtra(EXTRA_PRESENT,false)//是否有电池

    val level=intent.getIntExtra(EXTRA_LEVEL,-1)//取得电池的剩余容量

    val scale=intent.getIntExtra(EXTRA_SCALE,-1)//取得电池的总容量，通常为 100

    val br=level.toFloat()/scale.toFloat()//电池剩余量占比

    private val plugged=intent.getIntExtra(EXTRA_PLUGGED,-1)
    val pluggedString=
            when(plugged){
                BATTERY_PLUGGED_AC->{
                    "交流电电源"
                }
                BATTERY_PLUGGED_USB->{
                    "USB电源"
                }
                BATTERY_PLUGGED_WIRELESS->{
                    "无线电源"
                }
                else->{
                    "未供电"
                }
            }

    val voltage=//电池电压
            {
                val v=intent.getIntExtra(EXTRA_VOLTAGE,-1)
                if (v<0){
                    "未知"
                }else if (v>1000){
                    NumberFormat.getInstance().let {
                        it.minimumFractionDigits=1
                        it.maximumFractionDigits=2
                        it.format(v.toFloat()/1000f)+" v"
                    }
                }else{
                    "$v v"
                }
            }.invoke()

    val temperature=//取得电池的温度，单位是摄氏度
            {
                val tem=intent.getIntExtra(EXTRA_TEMPERATURE,-1)
                if (tem<0){
                    "未知"
                }else{
                    NumberFormat.getInstance().let {
                        it.minimumFractionDigits=1
                        it.maximumFractionDigits=1
                        it.format(tem.toFloat()/10f)+" ℃"
                    }
                }
            }.invoke()

    val technology=intent.getStringExtra(EXTRA_TECHNOLOGY)//取得电池的类型

    /**
     * 返回设备可使用时长，单位分钟
     */
    fun getCanUseTime():Int?{
        val currentBatteryTotal=br*(batteryCategory?:return null)
        val isGPSEnabled=try {
            val locationManager=BaseConstant.application.getSystemService(Context.LOCATION_SERVICE) as? LocationManager?:return null
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }catch (e:Exception){
            //MobclickAgent.reportError(BaseConstant.application,e)
            false
        }
        val displayMetrics=BaseConstant.application.resources.displayMetrics
        val speed=if (isGPSEnabled) 7.396593877221415e-7*2 else 7.396593877221415e-7
        return (currentBatteryTotal/speed/displayMetrics.widthPixels/displayMetrics.heightPixels).toInt()
    }

    /**
     *获得充满还需多长时间，单位分钟
     */
    fun batteryFullNeedTime():Int?{
        val noFullBattery=(1-br)*(batteryCategory?:return null)
        val speed=when(plugged){
            BATTERY_PLUGGED_AC->{
                9.65909091*2
            }
            BATTERY_PLUGGED_USB->{
                9.65909091
            }
            BATTERY_PLUGGED_WIRELESS->{
                9.65909091
            }
            else->{
                return 0
            }
        }
        return (noFullBattery/speed).toInt()
    }
}