package com.example.module_tool.activity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.module_base.utils.MyStatusBarUtil

import com.example.module_tool.R
import com.example.module_tool.base.BaseActivity
import com.example.module_tool.utils.ColorUtil
import kotlinx.android.synthetic.main.activity_horizontal_cjy.*
import java.text.DecimalFormat

/**
 * Author : Gupingping
 * Date : 2019/3/14
 * QQ : 464955343
 */
class HorizontalActivity : BaseActivity() {

    private lateinit var sensorManager: SensorManager

    override fun getLayoutId(): Int = R.layout.activity_horizontal_cjy

    //传感器事件监听
    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return
//            lg("HorizontalActivity==${event.values}")
            val decimalFormat = DecimalFormat()
            decimalFormat.applyPattern("0.0")
            val xString = decimalFormat.format(Math.abs(event.values[2]))
            val yString = decimalFormat.format(Math.abs(event.values[1]))
            horizontalX?.text = String.format(resources.getString(R.string.x), xString)
            horizontalY?.text = String.format(resources.getString(R.string.y), yString)
            horizontalView?.updateView(event.values, xString, yString)
        }
    }

    override fun isActionBar(): Boolean = false


    override fun initEvent() {
        HorizontalView_toolbar?.finishActivity(this);
    }

    override fun initView() {
        MyStatusBarUtil.setColor(this, ColorUtil.Theme)
        HorizontalView_toolbar?.setTitle("水平仪")
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sensorEventListener)

    }
}