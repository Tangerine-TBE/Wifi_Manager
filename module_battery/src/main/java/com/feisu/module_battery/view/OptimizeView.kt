package com.feisu.module_battery.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.feisu.module_battery.R

class OptimizeView @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defSty:Int=0):View(context,attributeSet,defSty) {

    private val outCirclePaint=Paint().apply {
        this.color= ContextCompat.getColor(context, R.color.optimize_view_out)
        this.strokeWidth=resources.displayMetrics.density*4
        this.style=Paint.Style.STROKE
        this.isAntiAlias=true
    }
    private val outCircleRect=RectF()
    private val outArcCount=4
    private val sweepAngle=360/(outArcCount*1.5f)

    private val innerCircleStrokePaint=Paint().apply {
        this.color= Color.WHITE
        this.strokeWidth=resources.displayMetrics.density*4
        this.style=Paint.Style.STROKE
        this.isAntiAlias=true
    }
    private val innerCirclePaint=Paint().apply {
        this.strokeWidth=resources.displayMetrics.density*4
        this.style=Paint.Style.FILL
    }
    private lateinit var sweepGradient:SweepGradient
    private val innerCircleMatrix=Matrix()
    private val valueAnimator=ValueAnimator.ofFloat(0f,360f).apply {
        this.interpolator=LinearInterpolator()
        this.repeatCount=ValueAnimator.INFINITE
        this.duration=1700
    }
    private var changeAngle=0f

    private val outAndInnerPadding=resources.displayMetrics.density*15+outCirclePaint.strokeWidth+innerCirclePaint.strokeWidth/2

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        outCircleRect.also {
            it.left=0f+outCirclePaint.strokeWidth/2
            it.top=0f+outCirclePaint.strokeWidth/2f
            it.right=width-outCirclePaint.strokeWidth/2f
            it.bottom=height-outCirclePaint.strokeWidth/2f
        }
        sweepGradient=SweepGradient(width/2f,height/2f,Color.TRANSPARENT,ContextCompat.getColor(context,R.color.optimize_view_animator))
        innerCirclePaint.shader = sweepGradient
        innerCircleMatrix.setRotate(0f,width/2f,height/2f)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until outArcCount){
            val startAngle=(1-i.toFloat()/outArcCount)*360-changeAngle
            canvas?.drawArc(outCircleRect,startAngle,sweepAngle,false,outCirclePaint)
        }

        canvas?.drawCircle(width/2f,height/2f,(outCircleRect.left+outCircleRect.right)/2f-outAndInnerPadding,innerCircleStrokePaint)

        canvas?.setMatrix(innerCircleMatrix)
        canvas?.drawCircle(width/2f,height/2f,(outCircleRect.left+outCircleRect.right)/2f-outAndInnerPadding,innerCirclePaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        valueAnimator.start()
        valueAnimator.addUpdateListener {
            val value=it.animatedValue
            if (value is Float){
                changeAngle=value
                innerCircleMatrix.setRotate(value,width/2f,height/2f)
                invalidate()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        valueAnimator.cancel()
    }

}