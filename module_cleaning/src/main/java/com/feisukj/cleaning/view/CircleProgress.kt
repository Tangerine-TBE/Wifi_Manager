package com.feisukj.cleaning.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircleProgress @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defSty:Int=0):View(context,attributeSet,defSty){
    var progressValue=0.3f
        set(value) {
            field = if (value>1f){
                1f
            }else{
                value
            }
        }
    private val progressPaint=Paint().apply {
        this.color=Color.WHITE
        this.strokeCap=Paint.Cap.ROUND
        this.isAntiAlias=true
        this.style=Paint.Style.STROKE
        this.strokeWidth=resources.displayMetrics.density*2
    }

    private val circleProgressPaint=Paint().apply {
        this.color=Color.WHITE
        this.alpha=255/2
        this.isAntiAlias=true
        this.style=Paint.Style.STROKE
        this.strokeWidth=resources.displayMetrics.density*2
    }

    private val circleProgressRect=RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val min= listOf(w,h).minOrNull()?:return
        circleProgressRect.also {
            it.left=circleProgressPaint.strokeWidth/2
            it.top=circleProgressPaint.strokeWidth/2
            it.right=min-circleProgressPaint.strokeWidth/2
            it.bottom=min-circleProgressPaint.strokeWidth/2
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        circleProgressRect
        canvas.drawArc(circleProgressRect,0f,360f,false,circleProgressPaint)
        canvas.drawArc(circleProgressRect,-90f,360f*progressValue,false,progressPaint)
    }
}