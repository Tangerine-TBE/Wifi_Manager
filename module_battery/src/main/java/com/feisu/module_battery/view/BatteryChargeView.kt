package com.feisu.module_battery.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.feisu.module_battery.R
import java.lang.IllegalArgumentException

class BatteryChargeView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?=null, defStyle:Int=0):View(context,attributeSet,defStyle) {
    var batterQuantity=0.2f
        set(value) {
            if (value>1f){
                throw IllegalArgumentException("电池电量范围为0到1")
            }
            if (value!=field){
                field=value
                invalidate()
            }
        }
    private var valueAnimator:ValueAnimator?=null
    private var currentAnimatorValue=batterQuantity

    private val topRectPaint=Paint().apply {
        this.color= ContextCompat.getColor(context, R.color.themeColor)
        this.style=Paint.Style.FILL
        this.strokeWidth=resources.displayMetrics.density
        this.isAntiAlias=true
    }
    private val outRectPaint=Paint().apply {
        this.color=ContextCompat.getColor(context, R.color.themeColor)
        this.style=Paint.Style.STROKE
        this.strokeWidth=resources.displayMetrics.density*3
        this.isAntiAlias=true
    }
    private val innerRectPaint=Paint().apply {
        this.color=ContextCompat.getColor(context, R.color.themeColor)
        this.style=Paint.Style.FILL
        this.isAntiAlias=true
    }
    private val topRectWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10f,resources.displayMetrics)
    private val topRectHeight=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30f,resources.displayMetrics)
    private val topRect=RectF()

    private val outRect=RectF()
    private val innerRect=RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        topRect.apply {
            this.left=(width-topRectWidth)
            this.right=width.toFloat()-topRectPaint.strokeWidth/2
            this.top=(height-topRectHeight)/2
            this.bottom=(height+topRectHeight)/2
        }
        outRect.apply {
            this.left=0f+outRectPaint.strokeWidth/2
            this.right=width.toFloat()-outRectPaint.strokeWidth/2-topRect.width()
            this.top=0f+outRectPaint.strokeWidth/2
            this.bottom=height.toFloat()-outRectPaint.strokeWidth/2
        }
        innerRect.apply {
            this.left=0f+outRectPaint.strokeWidth+4*resources.displayMetrics.density

            this.right=outRect.left+4*resources.displayMetrics.density+(outRect.width()-8*resources.displayMetrics.density)*currentAnimatorValue

            this.top=outRect.top+4*resources.displayMetrics.density+outRectPaint.strokeWidth/2

            this.bottom=outRect.bottom-outRectPaint.strokeWidth/2-4*resources.displayMetrics.density
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        innerRect.right=outRect.left+4*resources.displayMetrics.density+(outRect.width()-8*resources.displayMetrics.density)*currentAnimatorValue
        canvas?.drawRoundRect(topRect,resources.displayMetrics.density,resources.displayMetrics.density,topRectPaint)
        canvas?.drawRoundRect(outRect,resources.displayMetrics.density*3,resources.displayMetrics.density*3,outRectPaint)
        canvas?.drawRect(innerRect,innerRectPaint)
    }

    fun startChargeAnimation(){
        stopChargeAnimation()
        valueAnimator=ValueAnimator.ofFloat(batterQuantity,1f)
        valueAnimator?.repeatCount=ValueAnimator.INFINITE
        valueAnimator?.repeatMode=ValueAnimator.RESTART
        valueAnimator?.duration=3000
        valueAnimator?.addUpdateListener {
            val batteryValue=it.animatedValue
            if (batteryValue is Float){
                currentAnimatorValue=batteryValue
                invalidate()
            }
        }
        valueAnimator?.start()
    }

    fun stopChargeAnimation(){
        valueAnimator?.cancel()
        currentAnimatorValue=batterQuantity
        invalidate()
    }

    override fun onDetachedFromWindow() {
        stopChargeAnimation()
        super.onDetachedFromWindow()
    }
}