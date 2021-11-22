package com.example.module_tool.activity
//
//import android.annotation.SuppressLint
//import android.app.Service
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.location.LocationManager
//import android.os.Build
//import android.os.VibrationEffect
//import android.os.Vibrator
//import android.text.TextUtils
//import android.util.Log
//import android.view.View
//import androidx.core.content.ContextCompat
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.amap.api.location.AMapLocation
//import com.amap.api.location.AMapLocationClient
//import com.amap.api.location.AMapLocationClientOption
//import com.example.module_base.utils.GaoDeHelper
//import com.example.module_base.utils.MyStatusBarUtil
//import com.example.module_base.utils.SPUtil
//
//import com.example.module_tool.R
//import com.example.module_tool.base.BaseActivity
//
//import com.example.module_tool.utils.ColorUtil
//import kotlinx.android.synthetic.main.activity_compass_cjy.*
//import java.text.DecimalFormat
//import kotlin.math.abs
//
//
///**
// * Author : Gupingping
// * Date : 2019/3/13
// * QQ : 464955343
// */
//class CompassActivity : BaseActivity() {
//
//    private lateinit var sensorManager: SensorManager
//    var diffDegree=0f
//    var lastDegree360 = 0F
//    var degree360=0f
//    var degree = 0F
//    private var locationClient: AMapLocationClient? = null
//    private var pressure: Sensor? = null//压力传感器
//    private var accelerometer:Sensor?=null//加速度传感器
//    private val accelerometerResult=FloatArray(3)//加速度传感器结果
//    private var magnetic:Sensor?=null//磁场传感器
//    private val magneticResult=FloatArray(3)//磁场感应器结果
//    private val orientationResult=FloatArray(3)//方向感应器保存的结果
//    private var isCalibration:Boolean?=null
//    private val r=FloatArray(9)
//    private val I=FloatArray(9)
//    private var magneticFieldIntensity=0f
//    private val mfiIntent:Intent by lazy { Intent(ProofreadActivity.PROOFREAD_BR_ACTION) }
//
//
//    //传感器事件监听
//    val sensorEventListener = object : SensorEventListener {
//        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//
//        }
//
//        @SuppressLint("SetTextI18n")
//        override fun onSensorChanged(event: SensorEvent?) {
//            if (event == null) return
//            when(event.sensor.type){
//                Sensor.TYPE_PRESSURE->{
//                    val sPV = event.values[0]//气压值
//                    val df = DecimalFormat("0.0")
//                    df.roundingMode
//                    // 计算海拔
//                    val height = 44330000 * (1 - Math.pow(java.lang.Double.parseDouble(df.format(sPV)) / 1013.25,
//                            1.0.toFloat() / 5255.0))
//                    val s=resources.getString(R.string.hp)
//                    altitude.text = s.format(df.format(height),df.format(sPV))
//                }
//                Sensor.TYPE_ACCELEROMETER->{
//                    event.values.forEachIndexed { index, fl ->
//                        accelerometerResult[index]=fl
//                    }
//                }
//                Sensor.TYPE_MAGNETIC_FIELD->{
//                    event.values.forEachIndexed { index, fl ->
//                        magneticResult[index]=fl
//                    }
//
//                    //计算得出的方向
//                    val isRotationMatrix=SensorManager.getRotationMatrix(r,I,accelerometerResult,magneticResult)
//                    magneticFieldIntensity=(I[3] * r[0] + I[4] * r[3] + I[5] * r[6]) * magneticResult[0] +
//                            (I[3] * r[1] + I[4] * r[4] + I[5] * r[7]) * magneticResult[1] +
//                            (I[3] * r[2] + I[4] * r[5] + I[5] * r[8]) * magneticResult[2]
//                    mfiIntent.putExtra(ProofreadActivity.MFI_KEY,magneticFieldIntensity)
//                    if(magneticFieldIntensity>70){
//                        val redColor=ContextCompat.getColor(this@CompassActivity, R.color.red)
//                        if (jiaozhun?.currentTextColor!=redColor) {
//                            jiaozhun?.setTextColor(redColor)
//                        }
//                    }else{
//                        val grayColor=ContextCompat.getColor(this@CompassActivity, R.color.gray)
//                        if (jiaozhun?.currentTextColor!=grayColor) {
//                            jiaozhun?.setTextColor(grayColor)
//                        }
//                    }
//                    LocalBroadcastManager.getInstance(this@CompassActivity).sendBroadcast(mfiIntent)
//                    if(suoding.isSelected){
//                        return
//                    }
//
//                    SensorManager.getOrientation(r,orientationResult)
//                    degree = Math.toDegrees(orientationResult[0].toDouble()).toFloat()
//                    /**
//                     * 存在bug
//                     * 在正北和正南方向附近瞬间变化
//                     */
//
//
//                    degree360 = if (degree < 0) 360 + degree else degree
//                    if(abs(diffDegree)>3) {
//                        compass.`val` = degree360
//                        lastDegree360=degree360
//                        diffDegree=0f
//                        Log.e("角度", degree360.toString())
//                    }
//                    diffDegree+=degree360-lastDegree360
//                }
//
//            }
//        }
//    }
//
//    /**
//     * 检查是否要弹出校对提示，并弹出
//     */
//    private fun checkMagneticField(){
//
//        if (magneticFieldIntensity>70){
//            calibrationView.visibility=View.VISIBLE
//            isCalibration=false
//        }else{
//            if (isCalibration==false){
//                isCalibration=true
//                calibrationView.visibility=View.GONE
//                //震动
//                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
//                    val vibrationEffect=VibrationEffect.createOneShot(100L, 1)
//                    vibrator.vibrate(vibrationEffect)
//                }else {
//                    vibrator.vibrate(100)
//                }
//            }
//        }
//    }
//
//    override fun getLayoutId(): Int {
//        return R.layout.activity_compass_cjy
//    }
//    private val mChangeReceiver by lazy {
//        DateChangeReceiver()
//    }
//    @SuppressLint("MissingPermission")
//    override fun initView() {
//        MyStatusBarUtil.setColor(this,ColorUtil.COLOR_znz)
//        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
//        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//        magnetic=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
//        jiaozhun?.setOnClickListener {
//            startActivity(Intent(this,ProofreadActivity::class.java))
//        }
//        if (pressure == null) {
//            altitude?.setText(R.string.Unsupported)
//        }
//       // locate()
//        suoding.setOnClickListener {
//            it.isSelected=!it.isSelected
//            if (it.isSelected){
//                suoding.setText(R.string.shifang)
//            }else{
//                suoding.setText(R.string.suoding)
//            }
//        }
//        val mDataChange = IntentFilter().apply {
//            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
//        }
//        registerReceiver(mChangeReceiver, mDataChange)
//        mGaoDeHelper.startLocation()
//
//    }
//
//    private val mGaoDeHelper by lazy {
//        GaoDeHelper.getInstance().apply {
//            setListener {
//                if (it.longitude != 0.0 || it.latitude != 0.0) {
//                    latitude.text =it.latitude.toString()
//                    longitude.text =it.longitude.toString()
//                    locateTv.text = it.address
//                }
//            }
//        }
//    }
//
//    inner class DateChangeReceiver:BroadcastReceiver(){
//        override fun onReceive(context: Context, intent: Intent?) {
//            intent?.action.let {
//                when(it){
//                    LocationManager.PROVIDERS_CHANGED_ACTION -> {
//                        val lm = context.getSystemService(Service.LOCATION_SERVICE) as LocationManager
//                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) mGaoDeHelper.startLocation()
//                    }
//                }
//            }
//
//        }
//    }
//
//
//    override fun getStatusBarColor(): Int {
//        return R.color.black
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        sensorManager.registerListener(sensorEventListener, pressure, SensorManager.SENSOR_DELAY_NORMAL)
//        sensorManager.registerListener(sensorEventListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL)
//        sensorManager.registerListener(sensorEventListener,magnetic,SensorManager.SENSOR_DELAY_NORMAL)
//    }
//
//
//
//    override fun onDestroy() {
//        sensorManager.unregisterListener(sensorEventListener)
//        super.onDestroy()
//        unregisterReceiver(mChangeReceiver)
//
//
//    }
//}