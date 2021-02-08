package com.feisukj.cleaning.ui.activity

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.SeekBar
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.BanFeedHelper
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_ad.advertisement.InsertHelper
import com.example.module_base.cleanbase.toast


import com.feisukj.cleaning.R
import com.feisukj.cleaning.bean.BatteryInfo

import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_cooling_complete.*
import kotlinx.android.synthetic.main.activity_save_power.*
import kotlinx.android.synthetic.main.activity_save_power.leftBack


class SavePowerActivity :FragmentActivity(){
    companion object{
        private const val TO_SETTING_REQUEST_CODE=232
    }

    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val bluetoothAdapter by lazy { BluetoothAdapter.getDefaultAdapter()?:null }
    private lateinit var banFeedHelper: BanFeedHelper

    private val insertHelper by lazy {
        InsertHelper(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_power)
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .init()
        BatteryInfo(Intent(Intent.ACTION_BATTERY_CHANGED)).getCanUseTime().also {
            canUseTimeHour.text=((it?:0)/60).toString()
            canUseTimeMinute.text=((it?:0)%60).toString()
        }

        initSwitch()
        initListener()



        banFeedHelper=BanFeedHelper(this,top_ad,bottom_ad)
        banFeedHelper.showAd(AdType.POWERSAVING_PAGE)
        insertHelper.showAd(AdType.POWERSAVING_PAGE)
    }




    override fun onDestroy() {
        super.onDestroy()
        banFeedHelper.releaseAd()
        insertHelper.releaseAd()
    }



    private fun initSwitch(){
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER).let {
            gpsSwitch.isChecked=it
            if (!it){
                gps.visibility=View.GONE
            }
        }

        bluetoothAdapter?.isEnabled.let {
            if (it==true) {
                bluetoothSwitch.isChecked =it
            }else{
                bluetooth.visibility= View.GONE
            }
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            val switch=Settings.System.canWrite(applicationContext)
            if (!switch){
                if (screenTimeViewSwitch.currentView!=screenTimeSwitch){
                    screenTimeViewSwitch.showNext()
                }
                if (brightnessViewSwitch.currentView!=brightnessSwitch){
                    brightnessViewSwitch.showNext()
                }
                screenTimeSwitch.isChecked=false
                brightnessSwitch.isChecked=false
            }else{
                if (screenTimeViewSwitch.currentView!=screenTimeSeekBar){
                    screenTimeViewSwitch.showNext()
                }
                if (brightnessViewSwitch.currentView!=brightnessSeekBar){
                    brightnessViewSwitch.showNext()
                }
                screenTimeSeekBar.progress=Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)/1000
                brightnessSeekBar.progress=Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
            }
        }else{
            if (screenTimeViewSwitch.currentView!=screenTimeSeekBar){
                screenTimeViewSwitch.showNext()
            }
            if (brightnessViewSwitch.currentView!=brightnessSeekBar){
                brightnessViewSwitch.showNext()
            }
            screenTimeSeekBar.progress=Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)/1000
            brightnessSeekBar.progress=Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        }
//        Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE).let {
//            if (it==Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
//                brightnessSwitch.isChecked=true
//            }else if (it==Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL){
//                brightnessSwitch.isChecked=false
//            }
//        }
//        Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)//屏幕亮度值0-255
//        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)//设置当前屏幕亮度模式
//        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 1)//设置当前屏幕亮度值
    }

    private fun initListener(){
        leftBack.setOnClickListener {
            finish()
        }
        gpsSwitch.setOnCheckedChangeListener { buttonView, _ ->
            if (!buttonView.isPressed){
                return@setOnCheckedChangeListener
            }
            showDialog(Settings.ACTION_LOCATION_SOURCE_SETTINGS,gpsSwitch)
        }
        bluetoothSwitch.setOnCheckedChangeListener { buttonView, _ ->
            if (!buttonView.isPressed){
                return@setOnCheckedChangeListener
            }
            showDialog(Settings.ACTION_BLUETOOTH_SETTINGS,bluetoothSwitch)//BLUETOOTH_ADMIN
        }
        screenTimeSwitch.setOnCheckedChangeListener { buttonView, _ ->
            if (!buttonView.isPressed){
                return@setOnCheckedChangeListener
            }
            showWriteSettingDialog()
        }
        brightnessSwitch.setOnCheckedChangeListener { buttonView, _ ->
            if (!buttonView.isPressed){
                return@setOnCheckedChangeListener
            }
            showWriteSettingDialog()
        }
        screenTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress<10){
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 10*1000)
                    screenTimeSeekBar.progress=10
                }else{
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, progress*1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                toast("已设置锁屏时间为 ${screenTimeSeekBar.progress/60} 分钟 ${screenTimeSeekBar.progress%60} 秒")
            }
        })
        brightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                try {
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, progress)//设置当前屏幕亮度值
                }catch (e:IllegalArgumentException){
                    if (Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)!=255){
                        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun showDialog(action:String,switch: Switch){
        val massage=when(action){
            Settings.ACTION_LOCATION_SOURCE_SETTINGS->{
                if (switch.isChecked){
                    "打开GPS后手机将更耗电,是否前去打开?"
                }else{
                    "是否前去关闭GPS? 关闭后更加省电。"
                }
            }
            Settings.ACTION_BLUETOOTH_SETTINGS->{
                if (switch.isChecked){
                    "打开蓝牙后手机将更耗电,是否前去打开?"
                }else{
                    "是否前去关闭蓝牙? 关闭后更加省电。"
                }
            }
            else->{
                throw IllegalArgumentException("还没写这个action的代码...... 沙雕")
            }
        }
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(massage)
                .setNegativeButton("否") { _, _ ->
                    switch.isChecked=!switch.isChecked
                }
                .setPositiveButton("是"){_,_->
                    startActivityForResult(Intent(action),TO_SETTING_REQUEST_CODE)
                }
                .show()
    }

    private fun showWriteSettingDialog(){
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("使用该功能需要打开修改系统设置权限，是否前去开启修改系统设置权限？")
                .setPositiveButton("是"){ _, _ ->
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:$packageName"))
                        startActivityForResult(intent, TO_SETTING_REQUEST_CODE)
                    }else{

                    }
                }
                .setNegativeButton("否"){ _, _ ->
                    screenTimeSwitch.isChecked=false
                    brightnessSwitch.isChecked=false
                }
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==TO_SETTING_REQUEST_CODE){
            initSwitch()
        }
    }
}