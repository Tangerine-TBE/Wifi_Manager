package com.example.wifi_manager.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.SizeUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.base.BaseView
import com.example.wifi_manager.domain.HardwareBean
import com.example.wifi_manager.utils.DataProvider
import com.example.wifi_manager.utils.StepState

/**
 * @author: 铭少
 * @date: 2021/1/17 0017
 * @description：
 */
class HardwareProgressView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseView(context, attrs, defStyleAttr) {
    private val mPaint = Paint()
    private val mTextPaint = Paint()
    private var mWidth = 0f
    private var mHeight = 0f
    private val mBgColor = ContextCompat.getColor(context, R.color.theme_color)
    private val mCurrentProgressColor = ContextCompat.getColor(context, R.color.current_progress_bg_color)
    private val paintWidth = SizeUtils.dip2px(context, 15f).toFloat()
    private val margin = SizeUtils.dip2px(context, 32f).toFloat()

    private val bigText = SizeUtils.sp2px(context, 35f).toFloat()
    private val smallText = SizeUtils.sp2px(context, 16f).toFloat()
    private val miniText = SizeUtils.sp2px(context, 14f).toFloat()

    init {
        mPaint.apply {
            color = mBgColor
            strokeWidth = paintWidth
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        mTextPaint.apply {
            color = Color.WHITE
            strokeWidth = 1f
            textSize = bigText
            style = Paint.Style.FILL
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = widthSize.toFloat()
        mHeight = heightSize.toFloat()
        setMeasuredDimension(widthSize, heightSize)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawText(canvas)
        drawProgress(canvas)

    }

    private fun drawText(canvas: Canvas) {
        mTextPaint.textAlign = Paint.Align.LEFT
        mTextPaint.textSize = bigText
        canvas.drawText(currentData().progressTitle, margin,
                mHeight / 2 - SizeUtils.dip2px(context, 21f).toFloat(),
                mTextPaint)

        mTextPaint.textSize = smallText
        canvas.drawText("4", margin * 2,
                mHeight / 2 - SizeUtils.dip2px(context, 15f).toFloat(),
                mTextPaint)

        mTextPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(currentData().hint, mWidth - margin,
                mHeight / 2 - SizeUtils.dip2px(context, 15f).toFloat(),
                mTextPaint)

        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = miniText
        canvas.drawText("${currentData().title}", mWidth / 2,
                mHeight / 2 + SizeUtils.dip2px(context, 36f).toFloat(),
                mTextPaint)

    }

    private fun drawProgress(canvas: Canvas) {
        canvas.withTranslation(0f, mHeight / 2) {
            mPaint.color = mBgColor
            drawLine(margin, 0f, mWidth - margin, 0f, mPaint)
            if (currentData().progress > 0f) {
                mPaint.color = mCurrentProgressColor
                drawLine(margin, 0f, (mWidth - margin) * currentData().progress, 0f, mPaint)
              //  LogUtils.i("--drawProgress----${mWidth - margin}--------------${(mWidth - margin) * currentData().progress}----------${currentData().progress}------")
            }
        }
        mListener?.finish(currentState)
    }


    private fun currentData() = when (currentState) {
        StepState.ONE -> HardwareBean("正在优化：${DataProvider.hardwareList[0].title}", "正在优化", "1/", 0.25f)
        StepState.TWO -> HardwareBean("正在优化：${DataProvider.hardwareList[1].title}", "正在优化", "2/", 0.5f)
        StepState.THREE -> HardwareBean("正在优化：${DataProvider.hardwareList[2].title}", "正在优化", "3/", 0.75f)
        StepState.FOUR -> HardwareBean("正在优化：${DataProvider.hardwareList[3].title}", "正在优化", "4/", 1f)
        StepState.FIVE -> HardwareBean("已完成优化", "完成优化", "4/", 1f)
        else -> HardwareBean("正在优化：${DataProvider.hardwareList[0].title}", "正在优化", "0/", 0f)
    }


    private var currentState = StepState.NONE
    fun setProgressState(state: StepState) {
        currentState = state
        invalidate()
    }


    private var mListener: onFinishStepClickListener? = null

    fun setOnFinishStepClickListener(listener: onFinishStepClickListener) {
        mListener = listener
    }

    interface onFinishStepClickListener {
        fun finish(step: StepState)
    }

}