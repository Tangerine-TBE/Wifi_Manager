package com.example.wifi_manager.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.example.module_base.utils.SizeUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.base.BaseView

/**
 * @author: 铭少
 * @date: 2021/1/17 0017
 * @description：
 */
class HardwareProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseView(context, attrs, defStyleAttr) {
    private val mPaint = Paint()
    private val mTextPaint=Paint()
    private var mWidth=0f
    private var mHeight=0f
    private val mBgColor=ContextCompat.getColor(context, R.color.theme_color)
    private val mCurrentProgressColor=ContextCompat.getColor(context, R.color.current_progress_bg_color)
    private val paintWidth=SizeUtils.dip2px(context,15f).toFloat()
    private val margin = SizeUtils.dip2px(context, 32f).toFloat()

    private val bigText=SizeUtils.sp2px(context,35f).toFloat()
    private val smallText=SizeUtils.sp2px(context,16f).toFloat()
    private val miniText=SizeUtils.sp2px(context,14f).toFloat()

    init {
        mPaint.apply {
            color=mBgColor
            strokeWidth=paintWidth
            strokeCap=Paint.Cap.ROUND
            isAntiAlias=true
        }
        mTextPaint.apply {
            color=Color.WHITE
            strokeWidth=1f
            textSize=bigText
            style=Paint.Style.FILL
            textAlign=Paint.Align.LEFT
            isAntiAlias=true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = widthSize.toFloat()
        mHeight=heightSize.toFloat()
        setMeasuredDimension(widthSize,heightSize)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawText(canvas)
        drawProgress(canvas)

    }

    private fun drawText(canvas: Canvas) {
        mTextPaint.textAlign=Paint.Align.LEFT
        mTextPaint.textSize=bigText
        canvas.drawText("2/",margin,
            mHeight / 2-SizeUtils.dip2px(context,21f).toFloat(),
            mTextPaint)

        mTextPaint.textSize=smallText
        canvas.drawText("4",margin*2,
            mHeight / 2-SizeUtils.dip2px(context,15f).toFloat(),
            mTextPaint)

        mTextPaint.textAlign=Paint.Align.RIGHT
        canvas.drawText("已优化",mWidth - margin,
            mHeight / 2-SizeUtils.dip2px(context,15f).toFloat(),
            mTextPaint)

        mTextPaint.textAlign=Paint.Align.CENTER
        mTextPaint.textSize=miniText
        canvas.drawText("正在优化：优化WiFi连接引擎，智能网络加速",mWidth/2,
            mHeight / 2+SizeUtils.dip2px(context,36f).toFloat(),
            mTextPaint)

    }

    private fun drawProgress(canvas: Canvas) {
        canvas.withTranslation(0f, mHeight / 2) {
            mPaint.color = mBgColor
            drawLine(margin, 0f, mWidth - margin, 0f, mPaint)
            mPaint.color = mCurrentProgressColor
            drawLine(margin, 0f, 100f, 0f, mPaint)
        }
    }

}