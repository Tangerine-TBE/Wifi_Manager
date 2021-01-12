package com.example.wifi_manager.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.SweepGradient
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.example.wifi_manager.R
import com.example.wifi_manager.base.BaseView

/**
 * @author: 铭少
 * @date: 2021/1/11 0011
 * @description：
 */
class WifiSpeedTestView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseView(context, attrs, defStyleAttr) {
    private val mBgArcPaint= Paint()
    private val mCurrentArcPaint= Paint()
    private val mPinterPaint= Paint()
    private var mSweepGradient: SweepGradient?=null
    private val mPaintWidth=30f
    private var mWidth=0f
    private var mHeight=0f
    private var mRadius=0f
    private val mGradientColors = intArrayOf(

        R.color.gradient_two_color,
                R.color.gradient_one_color
    )

    init {
        mBgArcPaint.apply {
            color=ContextCompat.getColor(context,R.color.half_arc_color)
            strokeWidth=mPaintWidth
            style=Paint.Style.STROKE
            strokeCap= Paint.Cap.ROUND
            isAntiAlias=true
        }

        mCurrentArcPaint.apply {
            color=ContextCompat.getColor(context,R.color.half_arc_color)
            strokeWidth=mPaintWidth
            style=Paint.Style.STROKE
            strokeCap= Paint.Cap.ROUND
            isAntiAlias=true
        }

        mPinterPaint.apply {
            color=ContextCompat.getColor(context,R.color.pinter_color)
            strokeWidth=mPaintWidth
            style=Paint.Style.STROKE
            isAntiAlias=true
        }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth=widthSize.toFloat()
        mHeight=heightSize.toFloat()
        mRadius=widthSize/2f-mPaintWidth/2
        setMeasuredDimension(widthSize, heightSize)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBgArc(canvas)

        drawCurrentArc(canvas)
    }




    private fun drawBgArc(canvas: Canvas) {
        canvas.withTranslation(mWidth/2, mHeight-50){
            drawArc(-mRadius, -mRadius, mRadius, mRadius, -180f, 180f, false, mBgArcPaint)
        }
    }


    private fun drawCurrentArc(canvas: Canvas) {
        canvas.withTranslation(mWidth/2, mHeight-50){
            updateArcPaint()
            drawArc(-mRadius, -mRadius, mRadius, mRadius, -180f, 180f, false, mCurrentArcPaint)

        }
    }


    /**
     * 更新圆弧画笔
     */
    private fun updateArcPaint() {
        // 设置渐变
        val arcPostion = floatArrayOf(0.5f, 0.5f)
        val matrix = Matrix()
        matrix.setRotate(-180f, mWidth/2, mHeight/2)
        mSweepGradient = SweepGradient(mWidth/2, -mHeight/2, mGradientColors, arcPostion)
        mSweepGradient?.setLocalMatrix(matrix)
        mCurrentArcPaint.shader = mSweepGradient

    }

}