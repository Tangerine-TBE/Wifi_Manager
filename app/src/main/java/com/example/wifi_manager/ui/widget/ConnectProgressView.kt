package com.example.wifi_manager.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.SizeUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.base.BaseView
import com.example.wifi_manager.utils.StepState

/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.widget
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/21 19:29:39
 * @class describe
 */
class ConnectProgressView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseView(context, attrs, defStyleAttr) {
    private val mPaint=Paint()
    private var mWidth=0f
    private var mHeight=0f
    private val mPaintWidth= SizeUtils.dip2px(context,1f).toFloat()
    private val mCurrentProgressColor= ContextCompat.getColor(context, R.color.current_progress_bg_color)
    private val mBgProgressColor= ContextCompat.getColor(context, R.color.theme_color)
    init {
        mPaint.apply {
            color = mBgProgressColor
            strokeWidth=mPaintWidth
            style= Paint.Style.FILL
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

    private var mCurrentProgress= StepState.ONE
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.withTranslation (0f,mHeight/2){
            //背景
            mPaint.color=mBgProgressColor
            drawLine(0f, 0f, mWidth, 0f, mPaint)
            mPaint.color=mCurrentProgressColor
            //进度
            drawLine(0f,0f,mWidth* progress(),0f,mPaint)
           // LogUtils.i("-ConnectProgressView---$mWidth--------$mCurrentProgress--------${mWidth*progress()}---------")
        }
    }


    private fun progress() = when (mCurrentProgress) {
        StepState.ONE -> 0.2f
        StepState.TWO -> 0.4f
        StepState.THREE -> 0.6f
        StepState.FOUR -> 0.8f
        StepState.FIVE -> 1f
        else->0.2f
    }

    fun setProgressState(state: StepState) {
        mCurrentProgress=state
        invalidate()
    }


}