package com.feisukj.cleaning.recriver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.widget.Toast
import com.feisukj.cleaning.controller.flashlight.FlashLightManager


class LightSwitchReceiver:BroadcastReceiver() {
    companion object{
        val LIGHT_RECEIVER="com.feisukj.cleaning.light"
        private var isOpen=false

        private var isSystemFeature:Boolean?=null
    }
    private var cameraManager: CameraManager?=null
    private val mManager:FlashLightManager by lazy { FlashLightManager.getInstance() }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (isSystemFeature==null) {
            isSystemFeature = context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) == true
        }
        if (isSystemFeature==false){
            Toast.makeText(context,"当前手机不支持闪光灯",Toast.LENGTH_SHORT).show()
            return
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            cameraManager = context?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
            try {
                cameraManager?.setTorchMode("0", !isOpen)
                isOpen=!isOpen
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else {
            mManager.startFlashLight(!isOpen)
            isOpen=!isOpen
        }
    }
}