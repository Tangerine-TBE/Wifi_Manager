package com.example.module_tool.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Handler
import android.os.Looper
import android.util.Range
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.example.module_tool.R
import com.example.module_tool.base.BaseActivity
import com.example.module_tool.utils.DeviceUtils
import com.example.module_tool.utils.getCloselyPreSize
import com.example.module_tool.utils.toast
import kotlinx.android.synthetic.main.activity_mirror_cjy.*
import java.lang.Exception

class MirrorActivity : BaseActivity(){
    private val cameraManager: CameraManager by lazy { getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    private var cameraId:Int= CameraCharacteristics.LENS_FACING_BACK
        set(value) {
            field=value
            cameraCharacteristics=cameraManager.getCameraCharacteristics(field.toString())
            aeRange=cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE)
        }
    private lateinit var cameraCharacteristics:CameraCharacteristics
    private var cameraDevice:CameraDevice?=null
    private var session:CameraCaptureSession?=null
    private var captureRequestBuilder: CaptureRequest.Builder?=null
    private lateinit var openCallBack:CameraDevice.StateCallback
    private val childHandler:Handler by lazy { Handler(Looper.myLooper()!!) }
    private lateinit var surface: Surface
    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener
    private lateinit var sessionCallback:CameraCaptureSession.StateCallback
    private var aeRange:Range<Int>?=null
    private var surfaceTextureAvailable=false

    override fun getLayoutId()= R.layout.activity_mirror_cjy

    override fun initView() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        try {
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId.toString())
            aeRange=cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE)
            initCallBack()
            textureView?.surfaceTextureListener=surfaceTextureListener
            initClick()
            seekBar?.progress=50
        } catch (e: Exception) {
            toast("该功能暂时无法使用")
            finish()
        }

    }

    private fun initCallBack(){
        surfaceTextureListener=object :TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {

            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {

            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                surfaceTextureAvailable=true
                initCamera2()
            }
        }
        //打开相机回调
        openCallBack=object :CameraDevice.StateCallback(){
            override fun onOpened(camera: CameraDevice) {
                cameraDevice=camera
                val surfaceTexture=textureView.surfaceTexture
                val outputSizes=cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.getOutputSizes(SurfaceTexture::class.java)?.toList()
                if (outputSizes==null){
                    Toast.makeText(this@MirrorActivity,R.string.openCameraFail,Toast.LENGTH_SHORT).show()
                    return
                }
                val size=getCloselyPreSize(true, DeviceUtils.getScreenWidth(this@MirrorActivity),DeviceUtils.getScreenHeight(this@MirrorActivity),outputSizes)
                if (size!=null)
                    surfaceTexture!!.setDefaultBufferSize(size.width,size.height)
                surface= Surface(surfaceTexture)
                cameraDevice?.createCaptureSession(listOf(surface),sessionCallback,childHandler)
            }

            override fun onDisconnected(camera: CameraDevice) {

            }

            override fun onError(camera: CameraDevice, error: Int) {

            }

        }
        //相机连接回调

        sessionCallback=object :CameraCaptureSession.StateCallback(){
            override fun onConfigureFailed(session: CameraCaptureSession) {

            }
            override fun onConfigured(session: CameraCaptureSession) {
                try {
                this@MirrorActivity.session=session
                captureRequestBuilder=cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder?.addTarget(surface)
                // 自动对焦
                captureRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

                val request=captureRequestBuilder?.build()
                if(request!=null){
                    this@MirrorActivity.session?.setRepeatingRequest(request,null,childHandler)
                }
                }catch (e:Exception){
                    finish()
                    toast("该功能暂时无法使用")
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (surfaceTextureAvailable){
            initCamera2()
        }
    }

    override fun onPause() {
        session?.close()
        cameraDevice?.close()
        super.onPause()
    }

    private fun initCamera2(){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
            return

        try {
            cameraManager.openCamera(cameraId.toString(), openCallBack, childHandler)
        } catch (e:Exception) {
            finish()
            toast("该功能暂时无法使用")
        }

    }
    private fun initClick(){
        seekBar?.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setPicExposure(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }
    /**
     * 调整图像曝光量
     * CONTROL_AE_EXPOSURE_COMPENSATION
     */
    private fun setPicExposure(exposure:Int){
        try {
        val aeRangeBackups=aeRange
        if (aeRangeBackups==null){
            Toast.makeText(this,R.string.brightness,Toast.LENGTH_SHORT).show()
            return
        }
        val rangeLength=aeRangeBackups.upper-aeRangeBackups.lower//曝光量区间长度
        val e=exposure*rangeLength/100+aeRangeBackups.lower//该进度在对应的曝光量
        if(e !in aeRangeBackups) return
        captureRequestBuilder?.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION,e)
        val cr=captureRequestBuilder?.build()
        if (cr!=null)
            session?.setRepeatingRequest(cr,null,childHandler)
        }catch (e:Exception){
            finish()
            toast("该功能暂时无法使用")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        session?.close()
        cameraDevice?.close()
    }
}