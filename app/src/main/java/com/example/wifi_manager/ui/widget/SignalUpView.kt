package com.example.wifi_manager.ui.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.example.module_base.utils.SizeUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.base.BaseView
import com.loc.dr
import kotlinx.coroutines.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.widget
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/20 16:06:38
 * @class describe
 */
class SignalUpView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseView(context, attrs, defStyleAttr) {
    private var mWidth = 0f
    private var mHeight = 0f
    private val  mInnerCirclePaint= Paint()
    private val mOutsideCirclePaint = Paint()
    private val mTextPaint = Paint()
    private val paintWidth =5f
    private val mTextSize =SizeUtils.sp2px(context,27f).toFloat()
    private val mTextRectPaint= Paint()
    private val mTextRect= RectF()
    private val mInnerColor=ContextCompat.getColor(context, R.color.home_bg)
    private val mRectColor=ContextCompat.getColor(context, R.color.theme_color)

    init {
        mInnerCirclePaint.apply {
            color = mInnerColor
            style=Paint.Style.FILL
            strokeWidth=paintWidth
            isAntiAlias=true
        }

        mOutsideCirclePaint.apply {
            color = mInnerColor
            style=Paint.Style.STROKE
            strokeWidth=paintWidth
            isAntiAlias=true
        }

        mTextRectPaint.apply {
            color = Color.BLACK
            style=Paint.Style.FILL
            strokeWidth=1f
            isAntiAlias=true

        }
        mTextPaint.apply {
            color = Color.WHITE
            style=Paint.Style.FILL_AND_STROKE
            strokeWidth=1f
            textAlign=Paint.Align.CENTER
            textSize=mTextSize
            isAntiAlias=true
        }



    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = widthSize.toFloat()
        mHeight = heightSize.toFloat()
        setMeasuredDimension(widthSize, widthSize)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
       // canvas.drawColor(Color.WHITE)
        canvas.withTranslation (mWidth/2,mWidth/2){
            drawSignalCircle()
            drawSignalText()

        }


    }


    private fun Canvas.drawSignalText() {
        drawText("${mCurrentHint}%",0f,-30f,mTextPaint)
    }

    private fun Canvas.drawSignalCircle() {
        for (i in 0 until mProgress) {
            mOutsideCirclePaint.alpha=255-(255/10)*i
            drawCircle(
                0f,
                0f,
                mWidth / 5 + ((mWidth / 2 - mWidth / 5 - paintWidth) / 7) * i,
                mOutsideCirclePaint
            )
        }
        drawCircle(0f, 0f, mWidth / 5.5f, mInnerCirclePaint)
    }


    private var mCurrentHint=0
    private var mOldCurrentHint=0
    fun startCurrentHint(currentHint:Int){
        ValueAnimator.ofInt(mOldCurrentHint, currentHint).apply {
            interpolator= AccelerateDecelerateInterpolator()
            duration=1000
            addUpdateListener {
                mCurrentHint = it.animatedValue as Int
                invalidate()
                mOldCurrentHint=currentHint
            }
        }.start()
    }

    fun setCurrentHint(currentHint:Int){
        mCurrentHint=currentHint
        invalidate()
    }


    private var mProgress=0
    private var mJob:Job?=null
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startAnimation(){
        mJob= CoroutineScope(Dispatchers.Main).launch {
            while (true){
                if (mProgress < 9) {
                    mProgress++
                } else {
                    mProgress=0
                }
                delay(500)
                invalidate()
            }
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopAnimation(){
        mJob?.cancel()
    }


}


