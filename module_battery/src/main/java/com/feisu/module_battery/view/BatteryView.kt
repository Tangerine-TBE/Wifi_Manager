package com.feisu.module_battery.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class BatteryView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?=null, defStyle:Int=0):View(context,attributeSet,defStyle) {
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

    private val topRectPaint=Paint().apply {
        this.color= Color.WHITE
        this.style=Paint.Style.FILL
    }
    private val outRectPaint=Paint().apply {
        this.color=Color.WHITE
        this.style=Paint.Style.STROKE
        this.strokeWidth=resources.displayMetrics.density*2
    }
    private val innerRectPaint=Paint().apply {
        this.color=Color.WHITE
        this.style=Paint.Style.FILL
    }
    private val topRectWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,7f,resources.displayMetrics)
    private val topRectHeight=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,3f,resources.displayMetrics)
    private val topRect=RectF()

    private val outRect=RectF()
    private val innerRect=RectF()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        topRect.apply {
            this.left=(width-topRectWidth)/2f
            this.right=(width+topRectWidth)/2f
            this.top=0f
            this.bottom=topRectHeight
        }
        outRect.apply {
            this.left=0f+outRectPaint.strokeWidth/2
            this.right=width.toFloat()-outRectPaint.strokeWidth/2
            this.top=0f+topRectHeight+2*resources.displayMetrics.density+outRectPaint.strokeWidth/2
            this.bottom=height.toFloat()
        }
        innerRect.apply {
            this.left=0f
            this.right=width.toFloat()
            this.top=outRect.top+outRect.height()*(1-batterQuantity)
            this.bottom=outRect.bottom-outRectPaint.strokeWidth/2
        }
        canvas?.drawRoundRect(topRect,resources.displayMetrics.density,resources.displayMetrics.density,topRectPaint)
        canvas?.drawRoundRect(outRect,resources.displayMetrics.density*3,resources.displayMetrics.density*3,outRectPaint)
        canvas?.drawRect(innerRect,innerRectPaint)
    }
}