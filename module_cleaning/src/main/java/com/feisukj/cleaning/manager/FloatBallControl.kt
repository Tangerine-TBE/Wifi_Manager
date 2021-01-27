package com.feisukj.cleaning.manager

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.example.module_base.cleanbase.BaseConstant

import com.feisukj.cleaning.R
import com.feisukj.cleaning.utils.Constant
import com.feisukj.cleaning.utils.UsageAccessHelp
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class FloatBallControl private constructor(val context: Context) {
    companion object{
        val instance: FloatBallControl by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { FloatBallControl(
            BaseConstant.application) }
    }

    private val executors by lazy {  ThreadPoolExecutor(0,1,30,TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>()) }
    private val windowManager by lazy { context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
    private val wmParams = WindowManager.LayoutParams()
    private var view:View?=null
    private var isAdded=false

    private val displayMetrics =context.resources.displayMetrics
    private val viewHeight=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,48f,displayMetrics).toInt()
    private val viewWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,125f,displayMetrics).toInt()
    private var currentX:Int=displayMetrics.widthPixels-viewWidth
    private var currentY:Int=displayMetrics.heightPixels-viewHeight
    private var group1: Group?=null
    private var group2: Group?=null
    private var gDes_g1:TextView?=null
    private var gSize_g1:TextView?=null
    private var unitGB_g1:TextView?=null
    private var gDes_g2:TextView?=null
    private var gSize_g2:TextView?=null
    private var unitGB_g2:TextView?=null
    var pendingIntent: PendingIntent?=null
    var isStop=false

    init {
        wmParams.packageName = context.packageName
        wmParams.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_SCALED
                or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }else{
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        wmParams.format = PixelFormat.RGBA_8888
        wmParams.gravity = Gravity.START or Gravity.TOP

        wmParams.width = viewWidth
        wmParams.height = viewHeight
        wmParams.x = currentX
        wmParams.y = currentY
        if (view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.suspension_ball_clean, null)
            viewOnTouchListener()
            group1=view?.findViewById<Group>(R.id.group1)
            group2=view?.findViewById<Group>(R.id.group2)
            gDes_g1=view?.findViewById<TextView>(R.id.gDes_g1)
            gSize_g1=view?.findViewById<TextView>(R.id.gSize_g1)
            unitGB_g1=view?.findViewById<TextView>(R.id.unitGB_g1)
            gDes_g2=view?.findViewById<TextView>(R.id.gDes_g2)
            gSize_g2=view?.findViewById<TextView>(R.id.gSize_g2)
            unitGB_g2=view?.findViewById<TextView>(R.id.unitGB_g2)
        }
    }
    private var lastX=0f
    private var lastY=0f
    private var moveLength=0
    @SuppressLint("ClickableViewAccessibility")
    private fun viewOnTouchListener(){
        view?.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN->{
                    lastX=motionEvent.rawX
                    lastY=motionEvent.rawY
                    moveLength=0
                }
                MotionEvent.ACTION_MOVE->{
                    val xMove=(motionEvent.rawX-lastX).toInt()
                    val yMove=(motionEvent.rawY-lastY).toInt()
                    val xMoveAbs= abs(xMove)
                    val yMoveAbs= abs(yMove)
                    if (xMoveAbs>=1||yMoveAbs>=1){
                        wmParams.y+= yMove
                        wmParams.x+= xMove
                        lastX=motionEvent.rawX
                        lastY=motionEvent.rawY
                        windowManager.updateViewLayout(view,wmParams)
                        moveLength+=(xMoveAbs+yMoveAbs)
                    }
                }
                MotionEvent.ACTION_UP->{
                    val valueAnimator:ValueAnimator
                    if (wmParams.x+viewWidth/2>displayMetrics.widthPixels/2){
                        valueAnimator=ValueAnimator.ofInt(wmParams.x,displayMetrics.widthPixels-viewWidth)
                        view.setBackgroundResource(R.drawable.shape_suspension_right)
                        group1?.visibility=View.VISIBLE
                        group2?.visibility=View.GONE
                    }else{
                        valueAnimator=ValueAnimator.ofInt(wmParams.x,0)
                        view.setBackgroundResource(R.drawable.shape_suspension_left)
                        group1?.visibility=View.GONE
                        group2?.visibility=View.VISIBLE
                    }
                    valueAnimator.interpolator= AccelerateInterpolator()
                    valueAnimator.duration=100
                    valueAnimator.addUpdateListener {
                        wmParams.x=it.animatedValue as Int
                        if (isAdded)
                            windowManager.updateViewLayout(view,wmParams)
                    }
                    valueAnimator.start()
                    if (moveLength<15){
                        FloatBallManager.fromFloatBall=true
                        pendingIntent?.send()
                    }else{
                        FloatBallManager.fromFloatBall=false
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    fun showFloatBall(){
        if (!isAdded) {
            try {
                windowManager.addView(view, wmParams)
                isAdded = true
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun dismissBallView(){
        if (isAdded) {
            try {
                windowManager.removeView(view)
                isAdded = false
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun updataBallView(x:Int,y:Int){
        try {
            wmParams.x=x
            wmParams.y=y
            windowManager.updateViewLayout(view,wmParams)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun updateViewShow(){
        isStop=false
        BaseConstant.mainHandler.postDelayed({
            executors.execute(updateViewShowRunnable)
        },500)
    }
    fun stopUpdate(){
        isStop=true
        showFloatBall()
    }

    fun setGSizeString(numberString:String){
        gSize_g1?.text=numberString
        gSize_g2?.text=numberString
    }
    fun setGSizeUnit(unitString:String){
        unitGB_g1?.text=unitString
        unitGB_g2?.text=unitString
    }
    fun setGDes(des:String){
        gDes_g1?.text=des
        gDes_g2?.text=des
    }
    private val updateViewShowRunnable:Runnable by lazy {//更新悬浮球是否显示的任务
        Runnable {
            while (!isStop){
                if (BaseConstant.isForeground){
                    if (isAdded) {
                        BaseConstant.mainHandler.post {
                            dismissBallView()
                        }
                    }
                }else{
                    val packageName=UsageAccessHelp.instance.getTopAppName(BaseConstant.application)
                    Log.e("packageName",packageName)
                    if (Constant.SYSTEM_DESKTOP_PACKAGENAME.contains(packageName)){
                        BaseConstant.mainHandler.post {
                            if (!isAdded)
                                showFloatBall()
                        }
                    }else{
                        if (isAdded) {
                            BaseConstant.mainHandler.post {
                                dismissBallView()
                            }
                        }
                    }
                }
                Thread.sleep(800)
            }
        }
    }
}