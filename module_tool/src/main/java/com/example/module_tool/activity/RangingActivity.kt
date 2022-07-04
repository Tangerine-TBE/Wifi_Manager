package com.example.module_tool.activity

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.*
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.os.*
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import android.util.Size
import android.util.SparseIntArray
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.feisukj.bean.RangingData
import com.example.module_tool.dialog.RangingHelpDialog
import com.example.module_tool.dialog.TallDialog
import kotlinx.android.synthetic.main.activity_ranging.*
import java.util.*
import kotlin.math.abs
import android.graphics.*
import android.view.*
import android.widget.TextView
import android.view.Gravity
import com.example.module_base.utils.SPUtil
import com.example.module_tool.R
import com.example.module_tool.utils.DeviceUtils
import com.example.module_tool.utils.GsonUtils
import com.example.module_tool.utils.getCloselyPreSize

class RangingActivity : AppCompatActivity(), View.OnClickListener {

    private var mCameraManager: CameraManager? = null//摄像头管理器
    private var childHandler: Handler? = null
    private var mainHandler: Handler? = null
    private var mCameraID: String? = null//摄像头Id 0 为后  1 为前
    private var mImageReader: ImageReader? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCameraDevice: CameraDevice? = null
    private lateinit var surface: Surface
    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener
    private var surfaceTextureAvailable=false
    private var tall = 0
    private val bean: RangingData by lazy {
        RangingData()
    }
    private var angle = 0f
    private var isOnFloor = true
        set(value) {
            field = value
            tackCount = 0
            if (isOnFloor){
                type1.setBackgroundResource(R.drawable.shape_ranging_floor_n)
                type2.setBackgroundResource(R.drawable.shape_ranging_floor_y)
            }else{
                type1.setBackgroundResource(R.drawable.shape_ranging_floor_y)
                type2.setBackgroundResource(R.drawable.shape_ranging_floor_n)
            }
        }
    private var tackCount = 0
        set(value) {
            field = value
            when(value){
                0 -> if (isOnFloor) setTip("靶心对准物体","底部","后按下","#ff0000") else setTip("靶心对准物体","正下方的地面","后按下","#ff0000")
                1 -> if (isOnFloor) setTip("靶心对准物体","顶部","后按下","#ff0000") else setTip("靶心对准物体","底部","后按下","#ff0000")
                2 -> if (!isOnFloor) setTip("靶心对准物体","顶部","后按下","#ff0000")
            }
            toVibrator()
        }

    private lateinit var sensorManager: SensorManager

    //传感器事件监听
    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event == null) return
//            val xString = Math.abs(event.values[2])
//            val yString = Math.abs(event.values[1])
            angle = abs(event.values[1])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranging)
        initCallBack()
        take.setOnClickListener(this)
        type.setOnClickListener(this)
        top_tip.setOnClickListener {
            top_tip.visibility = View.GONE
        }
        goBack.setOnClickListener {
            finish()
        }
        ll_tall.setOnClickListener {
            TallDialog(this){
                tall = it
                SPUtil.getInstance().putInt(TALL_KEY,tall)
                tv_tall.text = "身高：$tall"
            }.show()
        }
        help.setOnClickListener {
            RangingHelpDialog(this).show()
        }
        textureView.surfaceTextureListener=surfaceTextureListener
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        tall = SPUtil.getInstance().getInt(TALL_KEY,0)
        if (tall == 0){
            TallDialog(this){
                tall = it
                SPUtil.getInstance().putInt(TALL_KEY,tall)
                tv_tall.text = "身高：$tall"
            }.show()
        }else
            tv_tall.text = "身高：$tall"

//        BaseApplication.handler.postDelayed({
//            showGuide1()
//        },500)
    }
    private fun initCallBack(){
        surfaceTextureListener=object : TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                surfaceTextureAvailable=true
                initCamera2()
            }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            take.id -> {
                when(tackCount){
                    0 -> {
                        bean.angle1 = angle
                        tackCount++
                    }
                    1 -> {
                        bean.angle2 = angle
                        if (isOnFloor){
                            bean.isOnFloor = isOnFloor
                            bean.cameraHeight = tall
                            val intent = Intent(this, RangingResultActivity::class.java)
                            intent.putExtra(RangingResultActivity.RESULT_KEY, GsonUtils.GsonString(bean))
                            startActivity(intent)
                            tackCount = 0
                        }else
                            tackCount++
                    }
                    else -> {
                        bean.angle3 = angle
                        if (!isOnFloor){
                            bean.isOnFloor = isOnFloor
                            bean.cameraHeight = tall
                            val intent = Intent(this, RangingResultActivity::class.java)
                            intent.putExtra(RangingResultActivity.RESULT_KEY,GsonUtils.GsonString(bean))
                            startActivity(intent)
                            tackCount = 0
                        }
                    }
                }
            }
            type.id -> isOnFloor = !isOnFloor
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus&&SPUtil.getInstance().getBoolean("needGuide",true)){
            showGuide1()
        }
    }

    private fun showGuide1(){
        SPUtil.getInstance().putBoolean("needGuide",false)
        val imageView = ImageView(this)
        imageView.setImageResource(R.mipmap.ic_ranging_guide_1)
        val popupWindow = PopupWindow(
            imageView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val lp = window.attributes
        lp.alpha = 0.5f
        window.attributes = lp

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.setTouchInterceptor(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                v.performClick()
                showGuide2()
                popupWindow.dismiss()
                return true
            }
        })
        imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        val location = IntArray(2)
        bottomView.getLocationOnScreen(location)
        popupWindow.showAtLocation(bottomView, Gravity.NO_GRAVITY,location[0],location[1]- imageView.measuredHeight)
    }
    private fun showGuide2(){
        val imageView = ImageView(this)
        imageView.setImageResource(R.mipmap.ic_ranging_guide_2)
        val popupWindow = PopupWindow(
            imageView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setOnDismissListener {
            val lp2 = window.attributes
            lp2.alpha = 1f
            window.attributes = lp2
        }

        popupWindow.isOutsideTouchable = true
        popupWindow.setTouchInterceptor(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                v.performClick()
//                showGuide3("靶心对准物体底部按下",true)
                popupWindow.dismiss()
                toVibrator()
                return true
            }
        })
        popupWindow.showAsDropDown(type)
    }

    private fun showGuide3(text: String,haveNext: Boolean){
        val view = View.inflate(this,R.layout.dialog_ranging_guide,null)
        val textView = view.findViewById<TextView>(R.id.text)
        textView.text = text
        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val lp = window.attributes
        lp.alpha = 0.7f
        window.attributes = lp

        popupWindow.setOnDismissListener {
            if (!haveNext){
                val lp2 = window.attributes
                lp2.alpha = 1f
                window.attributes = lp2
            }
        }

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.setTouchInterceptor(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                v.performClick()
                if (haveNext)
                    showGuide3("靶心对准物体顶部按下",false)
                popupWindow.dismiss()
                return true
            }
        })
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        val location = IntArray(2)
        bottomTip.getLocationOnScreen(location)
        popupWindow.showAtLocation(bottomTip, Gravity.NO_GRAVITY,location[0],location[1])
    }

    /**
     * 震动和平移动画
     * */
    private fun toVibrator(){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val vibrationEffect= VibrationEffect.createOneShot(100L, 1)
            vibrator.vibrate(vibrationEffect)
        }else {
            vibrator.vibrate(100)
        }
        val animation = TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,0f,TranslateAnimation.RELATIVE_TO_SELF,0f,TranslateAnimation.RELATIVE_TO_SELF,0f,TranslateAnimation.RELATIVE_TO_SELF,0.1f)
        animation.duration = 200
        animation.repeatMode = TranslateAnimation.REVERSE
        animation.repeatCount = 2
        animation.interpolator = LinearInterpolator()
        bottomTip.clearAnimation()
        bottomTip.startAnimation(animation)
    }

    private fun setTip(startStr: String, centerStr: String, endStr: String, centerColor: String){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tv_bottomTip.text =
                Html.fromHtml("${startStr}<font color=\"${centerColor}\">${centerStr}</font>$endStr", Html.FROM_HTML_MODE_LEGACY)
        } else {
            tv_bottomTip.text = Html.fromHtml("${startStr}<font color=\"${centerColor}\">${centerStr}</font>$endStr")
        }
    }

    override fun onResume() {
        super.onResume()
        if (surfaceTextureAvailable){
            initCamera2()
        }
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL)
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
            mCameraManager?.openCamera(mCameraID!!, stateCallback, mainHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
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

    private fun configureTextureViewTransform(viewWidth: Int, viewHeight: Int,mPreviewSize: Size) {
        if (null == textureView) {
            return
        }
        val rotation = windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(0f, 0f, mPreviewSize.height.toFloat(), mPreviewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale: Float = Math.max(
                viewHeight.toFloat() / mPreviewSize.height,
                viewWidth.toFloat() / mPreviewSize.width
            )
            matrix.postScale(scale, scale, centerX, centerY)
            matrix.postRotate(90f * (rotation - 2f), centerX, centerY)
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180f, centerX, centerY)
        }
        textureView.setTransform(matrix)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePreview() {
        try {
            // 创建预览需要的CaptureRequest.Builder
            val previewRequestBuilder = mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            val surfaceTexture=textureView.surfaceTexture
            if (mCameraID==null) return
            val outputSizes=mCameraManager?.getCameraCharacteristics(mCameraID!!)?.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.getOutputSizes(
                SurfaceTexture::class.java)?.toList()?:return
            val size= getCloselyPreSize(true, DeviceUtils.getScreenWidth(this), DeviceUtils.getScreenHeight(this),outputSizes)
            if (size!=null&&surfaceTexture!=null){
                surfaceTexture.setDefaultBufferSize(size.width,size.height)
                configureTextureViewTransform(textureView.width,textureView.height,size)
            }
            surface= Surface(surfaceTexture)
            previewRequestBuilder?.addTarget(surface)

            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            val callback = object : CameraCaptureSession.StateCallback() {
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

            }
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.P){
                mCameraDevice?.createCaptureSession(
                    SessionConfiguration(
                        SessionConfiguration.SESSION_REGULAR,
                    Collections.singletonList(OutputConfiguration(surface)), mainExecutor,callback)
                )
            }else{
                mCameraDevice?.createCaptureSession(listOf(surface, mImageReader?.surface), callback, childHandler)
            }


        } catch (e: CameraAccessException) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCameraDevice?.close()
        mCameraCaptureSession?.close()
        sensorManager.unregisterListener(sensorEventListener)
    }

    companion object {

        private val TALL_KEY = "tall_key"

        private var ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 0)
            ORIENTATIONS.append(Surface.ROTATION_90, 270)
            ORIENTATIONS.append(Surface.ROTATION_180, 180)
            ORIENTATIONS.append(Surface.ROTATION_270, 90)
        }
    }

}