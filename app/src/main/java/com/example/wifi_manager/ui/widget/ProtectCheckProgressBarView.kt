package com.example.wifi_manager.ui.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.SizeUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.base.BaseView
import com.tamsiree.rxkit.RxDeviceTool

/**
 * @author: 铭少
 * @date: 2021/1/17 0017
 * @description：
 */
class ProtectCheckProgressBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseView(context, attrs, defStyleAttr) {
    private val mPaint=Paint()
    private var mWidth=0f
    private var mHeight=0f
    private val mRadius=SizeUtils.dip2px(context,4f).toFloat()
    private val mPaintWidth=SizeUtils.dip2px(context,2f).toFloat()
    private val mCurrentProgressColor=ContextCompat.getColor(context, R.color.current_progress_bg_color)
    private val mBgProgressColor=ContextCompat.getColor(context, R.color.progress_bg_color)

    init {
        mPaint.apply {
            color = mBgProgressColor
            strokeWidth=mPaintWidth
            style=Paint.Style.FILL
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




    private var mCurrentProgress=0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withTranslation (0f,mHeight/2){
            //背景
            mPaint.color=mBgProgressColor
            drawLine(0f, 0f, mWidth, 0f, mPaint)
            mPaint.color=mCurrentProgressColor
            //进度
            drawLine(0f,0f,mCurrentProgress,0f,mPaint)
            //圆
            drawCircle(mCurrentProgress+mRadius,0f,mRadius,mPaint)
        }
    }

    fun startProtectCheck(){
        LogUtils.i("-------startProtectCheck------------$mWidth---------------")
        ValueAnimator.ofFloat(0f, RxDeviceTool.getScreenWidth(context).toFloat()).apply {
            duration = 5000
            interpolator= AccelerateDecelerateInterpolator()
            addUpdateListener {
                mCurrentProgress = it.animatedValue as Float
                invalidate()
            }

           doOnEnd {
               LogUtils.i("-------startProtectCheck--------onAnimationEnd---")
           }


        }.start()
    }
}