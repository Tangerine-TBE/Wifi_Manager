package com.example.wifi_manager.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.example.wifi_manager.R
import com.example.wifi_manager.base.BaseView
import kotlinx.coroutines.*

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.widget
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/11 10:52:54
 * @class describe
 */
class ScanWifiLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseView(context, attrs, defStyleAttr) {
    private val mOutSideCircle = Paint()
    private val mInnerCircle = Paint()
    private var mWidth=0f
    private var mHeight=0f
    private val mPaintWidth=2f
    private var mInnerRadius=0f
    private var mOutsideRadius=0f
    private var mRotateJob:Job?=null

    init {
        mOutSideCircle.apply {
            style=Paint.Style.STROKE
            color=ContextCompat.getColor(context, R.color.outside_color)
            strokeWidth=mPaintWidth
            isAntiAlias=true
        }
        mInnerCircle.apply {
            style=Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.inner_color)
            strokeWidth=mPaintWidth
            isAntiAlias=true
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = widthSize.toFloat()
        mHeight=heightSize.toFloat()
        mOutsideRadius=mWidth/2f-mPaintWidth/2
        mInnerRadius=mWidth/3f
        setMeasuredDimension(widthSize,heightSize)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withTranslation(mWidth/2,mHeight/2) {
            //外圆
            drawArc(-mOutsideRadius, -mOutsideRadius, mOutsideRadius, mOutsideRadius, mStartAngle, 270f, false, mOutSideCircle)
            //内圆
            drawArc(-mInnerRadius,-mInnerRadius,mInnerRadius,mInnerRadius,-mStartAngle,-180f,false,mInnerCircle)
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == 0) startRotate() else stopRotate()
    }

    private var mStartAngle=0f
    private fun startRotate(){
        mRotateJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                if (mStartAngle < 2 * 1000 * 30) {
                    mStartAngle += 30f
                } else {
                    mStartAngle=0f
                }
                invalidate()
                delay(100)
            }
        }

    }

    private fun stopRotate(){
        mRotateJob?.cancel()
    }
}