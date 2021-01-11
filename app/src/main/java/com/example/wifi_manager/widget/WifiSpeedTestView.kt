package com.example.wifi_manager.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
    private val mHalfArcPaint= Paint()
    private val mScalePaint= Paint()
    private val mPinterPaint= Paint()
    private val mPaintWidth=30f
    private var mWidth=0f
    private var mHeight=0f
    private var mRadius=0f

    init {
        mHalfArcPaint.apply {
            color=ContextCompat.getColor(context,R.color.half_arc_color)
            strokeWidth=mPaintWidth
            style=Paint.Style.STROKE
            isAntiAlias=true
        }

        mScalePaint.apply {
            color=ContextCompat.getColor(context,R.color.scale_normal_color)
            strokeWidth=mPaintWidth
            style=Paint.Style.STROKE
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
        drawHalfArc(canvas)
        drawScale(canvas)
    }

    private fun drawScale(canvas: Canvas) {
        canvas.withTranslation(mWidth/2, mHeight/2){
            drawCircle(0f,0f,20f,mPinterPaint)
            for (i in 0..20){
                drawPoint(0f,-mHeight/2+15f,mPinterPaint)
                //drawLine(0f,-mHeight/2,0f,-mHeight/2f+mPaintWidth+5,mPinterPaint)
                rotate(18f,0f,0f)
            }
        }



    }

    private fun drawHalfArc(canvas: Canvas) {
        canvas.withTranslation(mWidth/2, mHeight/2){
            drawArc(-mRadius, -mRadius, mRadius, mRadius+mPaintWidth, 10f, -200f, false, mHalfArcPaint)
        }
    }


}