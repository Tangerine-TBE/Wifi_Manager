package com.example.module_tool.activity
import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.SPUtil
import com.example.module_tool.R
import com.example.module_tool.base.BaseActivity
import com.example.module_tool.utils.ColorUtil
import com.example.module_tool.utils.DeviceUtils
import com.example.module_tool.utils.getCloselyPreSize
import com.example.module_tool.utils.toast
import com.example.module_tool.widget.DiyToolbar
import com.example.module_tool.widget.RectControlView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_distance_cjy.*
import java.text.DecimalFormat


/**
 * Author : Gupingping
 * Date : 2019/3/4
 * QQ : 464955343
 */
class DistanceActivity : BaseActivity(), RectControlView.OnRulerHeightChangedListener, View.OnClickListener {

    private var mCameraManager: CameraManager? = null//摄像头管理器
    private var childHandler: Handler? = null
    private var mainHandler: Handler? = null
    private var mCameraID: String? = null//摄像头Id 0 为后  1 为前
    private var mImageReader: ImageReader? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCameraDevice: CameraDevice? = null
    private var targetHeight = 0F//用户输入的物体实际高度
    private var rulerHeight = 0F//屏幕测量高度
    private var hasCapture = false
    private lateinit var surface: Surface
    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener
    private lateinit var bottomDialog: BottomSheetDialog
    private var currentDisFactor=0
    private var surfaceTextureAvailable=false

    override fun getLayoutId(): Int = R.layout.activity_distance_cjy

    override fun isActionBar(): Boolean = false

    override fun initView() {
        MyStatusBarUtil.setColor(this,ColorUtil.Theme)
        textureView_toolbar?.setTitle("距离测算")
        initBottomDialog()
        initCallBack()
        textureView.surfaceTextureListener=surfaceTextureListener
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s) && s?.startsWith(".") != true) {
                    targetHeight = s.toString().toFloat()
                    calculateAndDisplay(rulerHeight)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        editText.isFocusable = true
        editText.requestFocus()
    }

    private fun initBottomDialog(){
        currentDisFactor= SPUtil.getInstance().getInt(DISTANCE_FACTOR_KEY,34)
        var disFactor=currentDisFactor
        bottomDialog= BottomSheetDialog(this)
        val view=LayoutInflater.from(this).inflate(R.layout.dialog_distance_calibration,null)
        val currentDisFactorV=view.findViewById<TextView>(R.id.current)
        val disFactorV=view.findViewById<TextView>(R.id.disFactor)
        view.findViewById<ImageView>(R.id.less).setOnClickListener {
            if (disFactor>0) {
                disFactor--
            }
            disFactorV.text=disFactor.toString()
        }
        view.findViewById<ImageView>(R.id.add).setOnClickListener {
            disFactor++
            disFactorV.text=disFactor.toString()
        }
        view.findViewById<View>(R.id.ok).setOnClickListener {
            SPUtil.getInstance().putInt(DISTANCE_FACTOR_KEY,disFactor)
            currentDisFactor=disFactor
            calculateAndDisplay(rulerHeight)
            bottomDialog.dismiss()
        }
        bottomDialog.setContentView(view)

        textureView_toolbar?.isShowRightIcon(true);
        textureView_toolbar?.setOnShowOnClickListener(DiyToolbar.OnShowOnClickListener {
            currentDisFactor.toString().also {
                currentDisFactorV.text=it
                disFactorV.text=it
            }
            bottomDialog.show()
        })


    }

    override fun initEvent() {
       textureView_toolbar?.finishActivity(this)
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

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            distanceTake.id -> {
                if (!hasCapture) {
                    takePicture()
                    hasCapture = true
                    distanceTake.setText(R.string.preview)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        initCamera2()
                        hasCapture = false
                        distanceTake.setText(R.string.photograph)
                    }
                }
            }
        }
    }

    override fun onRulerHeightChanged(rulerHeight: Float, bitmap: Bitmap?) {

        this.rulerHeight = rulerHeight
        calculateAndDisplay(rulerHeight)
    }

    private fun calculateAndDisplay(rulerHeight: Float) {
        if (targetHeight > 0 && rulerHeight > 0) {
            val distance = targetHeight / rulerHeight * Math.pow(currentDisFactor.toDouble(), 2.0)
            val decimalFormat = DecimalFormat()
            decimalFormat.applyPattern("0.00")
            resultTv.visibility = View.VISIBLE
            resultTv.text = String.format(resources.getString(R.string.distance), decimalFormat.format(distance))
        }
    }

    override fun onResume() {
        super.onResume()
        if (surfaceTextureAvailable){
            initCamera2()
        }
    }

    override fun onPause() {
        mCameraDevice?.close()
        mCameraCaptureSession?.close()
        super.onPause()
    }

    /**
     * 初始化Camera2
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initCamera2() {
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val handlerThread = HandlerThread("Camera2")
        handlerThread.start()
        childHandler = Handler(handlerThread.looper)
        mainHandler = Handler(mainLooper)

        //获取可用的摄像头
        for (item in mCameraManager!!.cameraIdList) {
            //获取相机相关参数
            val cameraCharacteristics = mCameraManager!!.getCameraCharacteristics(item)
            //不使用前置摄像头
            val facing = cameraCharacteristics[CameraCharacteristics.LENS_FACING]
            if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                continue
            }
            val isAvailable = cameraCharacteristics[CameraCharacteristics.FLASH_INFO_AVAILABLE]
            if (isAvailable == true) {
                mCameraID = item
            }
        }

        if (mCameraID != null) {
            mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1)
            mImageReader?.setOnImageAvailableListener(ImageReader.OnImageAvailableListener { reader ->
                //可以在这里处理拍照得到的临时照片 例如，写入本地
                mCameraDevice?.close()
                textureView?.visibility = View.GONE
                // 拿到拍照照片数据
                val image = reader.acquireNextImage()
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)//由缓冲区存入字节数组
            }, mainHandler)

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
        } else {
            toast("手机不支持")
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
            val size=getCloselyPreSize(true, DeviceUtils.getScreenWidth(this@DistanceActivity),DeviceUtils.getScreenHeight(this@DistanceActivity),outputSizes)
            if (size!=null)
                surfaceTexture!!.setDefaultBufferSize(size.width,size.height)
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
        } catch (e: CameraAccessException) {
        }
    }

    /**
     * 拍照
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePicture() {
        if (mCameraDevice == null) return
        // 创建拍照需要的CaptureRequest.Builder
        val captureRequestBuilder: CaptureRequest.Builder
        try {
            captureRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            val surface=mImageReader?.surface?:return
            captureRequestBuilder.addTarget(surface)
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            // 获取手机方向
            val rotation = windowManager.defaultDisplay.rotation
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            //拍照
            val mCaptureRequest = captureRequestBuilder.build()
            mCameraCaptureSession?.capture(mCaptureRequest, null, childHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mCameraDevice?.close()
        mCameraCaptureSession?.close()
    }

    companion object {
        private val DISTANCE_FACTOR_KEY="d_f_k"
        private var ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

}