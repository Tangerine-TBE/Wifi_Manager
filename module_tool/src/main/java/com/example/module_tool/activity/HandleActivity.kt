package com.example.module_tool.activity

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import com.example.module_base.utils.MyStatusBarUtil

import com.example.module_tool.R
import com.example.module_tool.base.BaseActivity
import com.example.module_tool.utils.ColorUtil
import com.example.module_tool.utils.DeviceUtils
import com.example.module_tool.utils.getCloselyPreSize
import kotlinx.android.synthetic.main.activity_handle_cjy.*

import java.util.*

/**
 * Author : Gupingping
 * Date : 2019/3/18
 * QQ : 464955343
 */
class HandleActivity : BaseActivity() {
    private lateinit var sensorManager: SensorManager

    private var mCameraManager: CameraManager? = null//摄像头管理器
    private var childHandler: Handler? = null
    private var mainHandler: Handler? = null
    private var mCameraID: String? = null//摄像头Id 0 为后  1 为前
    private var mImageReader: ImageReader? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCameraDevice: CameraDevice? = null
    private var surfaceTextureAvailable=false

    private var values = FloatArray(9)
    private var acc = FloatArray(3)
    private var mag = FloatArray(3)
    private var accOrientation = FloatArray(3)//偏移方向
    private var orientation = FloatArray(3)//偏移角度
    private lateinit var surface: Surface
    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener

    override fun getLayoutId(): Int = R.layout.activity_handle_cjy

    override fun isActionBar(): Boolean = false

    override fun initView() {
        MyStatusBarUtil.setColor(this, ColorUtil.Theme)
        handle_toolbar?.setTitle("插画校对")
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        initCallBack()
        textureView.surfaceTextureListener=surfaceTextureListener
        handle_toolbar?.finishActivity(this)
    }

    private fun initCallBack(){
        surfaceTextureListener=object : TextureView.SurfaceTextureListener{
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
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME)
        if (surfaceTextureAvailable){
            initCamera2()
        }
    }

    override fun onPause() {
        mCameraDevice?.close()
        mCameraCaptureSession?.close()
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }

    /**
     * 初始化Camera2
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initCamera2() {
        val handlerThread = HandlerThread("Camera2")
        handlerThread.start()
        childHandler = Handler(handlerThread.looper)
        mainHandler = Handler(mainLooper)
        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT//后摄像头
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
        mImageReader?.setOnImageAvailableListener(ImageReader.OnImageAvailableListener { reader ->
            //可以在这里处理拍照得到的临时照片 例如，写入本地
        }, mainHandler)
        //获取摄像头管理
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            //打开摄像头
            val id=mCameraID?:return
            mCameraManager?.openCamera(id, stateCallback, mainHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mCameraCaptureSession?.close()
        mCameraDevice?.close()
        sensorManager.unregisterListener(sensorEventListener)

    }

    //传感器事件监听
    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                acc = event.values
            }
            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mag = event.values
            }

            if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
                orientation = event.values
            }
            if (acc.isNotEmpty() && mag.isNotEmpty()) {
                SensorManager.getRotationMatrix(values, null, acc, mag)
                SensorManager.getOrientation(values, accOrientation)
            }

            if (orientation.isNotEmpty()) {
//                lg("HandleActivity orientation x==${orientation[0]} y==${orientation[1]} z==${orientation[2]}")
//                lg("HandleActivity orientation x==${accOrientation[0]} y==${accOrientation[1]} z==${accOrientation[2]}")
                horizontal.updateView(accOrientation, orientation)
            }

//            val decimalFormat = DecimalFormat()
//            decimalFormat.applyPattern("0.0")
//            val zString = decimalFormat.format(Math.abs(event.values[2]))
//            val yString = decimalFormat.format(Math.abs(event.values[1]))
//            val xString = decimalFormat.format(Math.abs(event.values[0]))
//            lg("HandleActivity xString==$xString,yString==$yString,zString==$zString")
//            horizontal.updateView(event.values)
        }
    }
    /**
     * 摄像头创建监听
     */
    private val stateCallback = @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            //开启预览

            takePreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            if (null != mCameraDevice) {

                mCameraDevice?.close()
                mCameraDevice = null
            }
        }

        override fun onError(camera: CameraDevice, error: Int) {
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePreview() {
        try {
            // 创建预览需要的CaptureRequest.Builder
            val previewRequestBuilder = mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            val surfaceTexture=textureView.surfaceTexture
            if (mCameraID==null) return
            val outputSizes=mCameraManager?.getCameraCharacteristics(mCameraID!!)?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.getOutputSizes(SurfaceTexture::class.java)?.toList()?:return
            val size=getCloselyPreSize(true, DeviceUtils.getScreenWidth(this@HandleActivity),DeviceUtils.getScreenHeight(this@HandleActivity),outputSizes)
            if (size!=null)
                surfaceTexture?.setDefaultBufferSize(size.width,size.height)
            if (surfaceTexture != null) {
                surface= Surface(surfaceTexture)
            previewRequestBuilder?.addTarget(surface)
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            mCameraDevice?.createCaptureSession(listOf(surface, mImageReader?.surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession) {

                }

                override fun onConfigured(session: CameraCaptureSession) {
                    if (null == mCameraDevice) return
                    // 当摄像头已经准备好时，开始显示预览
                    mCameraCaptureSession = session

                    try {
                        // 自动对焦
                        previewRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                        // 打开闪光灯
                        previewRequestBuilder?.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                        // 显示预览
                        val previewRequest = previewRequestBuilder?.build()
                        if (previewRequest != null)
                            mCameraCaptureSession?.setRepeatingRequest(previewRequest, null, childHandler)
                    } catch (e: CameraAccessException) {
                    }
                }

            }, childHandler)
            }
        } catch (e: CameraAccessException) {
        }

    }

//    override fun getStatusBarColor(): Int {
//        return R.color.black
//    }
}