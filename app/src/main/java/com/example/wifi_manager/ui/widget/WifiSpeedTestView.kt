package com.example.wifi_manager.ui.widget

import android.content.Context
import android.graphics.*
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
    private val mLinePaint= Paint()
    private val mTrianglePath= Path()
    private val mPaintWidth=30f
    private var mWidth=0f
    private var mHeight=0f
    private var mRadius=0f
    private val startColor=ContextCompat.getColor(context, R.color.gradient_one_color)
    private val endColor=ContextCompat.getColor(context, R.color.gradient_two_color)

    init {
        mBgArcPaint.apply {
            color=ContextCompat.getColor(context,R.color.half_arc_color)
            strokeWidth=mPaintWidth
            style= Paint.Style.STROKE
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
            style=Paint.Style.FILL
            isAntiAlias=true
        }

        mLinePaint.apply {
            color=ContextCompat.getColor(context,R.color.pinter_color)
            strokeWidth=10f
            style=Paint.Style.STROKE
            pathEffect = CornerPathEffect(5f)
            strokeCap= Paint.Cap.ROUND
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
        mCurrentArcPaint.shader = LinearGradient(-mRadius, 0f, mRadius/2, -mRadius/2,startColor,
                endColor, Shader.TileMode.CLAMP)
    }



}