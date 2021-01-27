package com.feisu.module_battery.view

import android.content.Context
import android.graphics.*
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.feisu.module_battery.R


class CircleProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr:Int=0): View(context,attrs,defStyleAttr) {
    var progress=0.5f
        set(value) {
            field=value
            postInvalidate()
        }
    private val paint = Paint().apply {
        this.color= ContextCompat.getColor(context, R.color.themeColor)
        this.style= Paint.Style.STROKE
        this.strokeWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10f,resources.displayMetrics)
        this.isAntiAlias=true
        this.strokeCap=Paint.Cap.ROUND
    }
    private val bgPaint=Paint().apply {
        this.color= ContextCompat.getColor(context,R.color.circleProgressBg)
        this.style= Paint.Style.STROKE
        this.strokeWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10f,resources.displayMetrics)
        this.isAntiAlias=true
    }
    private val innerCircleBackgroundPaint=Paint().apply {
        this.color=Color.TRANSPARENT
    }
    private val rectF= RectF()

    init {
        val t=context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView)
        innerCircleBackgroundPaint.color=t.getColor(R.styleable.CircleProgressView_inner_background,Color.TRANSPARENT)
        paint.color=t.getColor(R.styleable.CircleProgressView_progressColor,ContextCompat.getColor(context,R.color.themeColor))
        bgPaint.color=t.getColor(R.styleable.CircleProgressView_progressBgColor,ContextCompat.getColor(context,R.color.circleProgressBg))
        t.getDimension(R.styleable.CircleProgressView_circle_stroke_width,resources.displayMetrics.density*5).also {
            bgPaint.strokeWidth=it
            paint.strokeWidth=it+1
        }
        t.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val min:Float
        val paintStrokeWidth=paint.strokeWidth
        listOf(w,h).min().also {
            it?:return
            min=it-paintStrokeWidth/2
        }
        rectF.left=0f+paintStrokeWidth/2
        rectF.top=0f+paintStrokeWidth/2
        rectF.right= min
        rectF.bottom= min
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(width/2f,height/2f,rectF.width()/2-bgPaint.strokeWidth/2,innerCircleBackgroundPaint)
        canvas?.drawArc(rectF,0f,360f,false,bgPaint)
        canvas?.drawArc(rectF,-90f,-progress*360,false,paint)
    }

    fun setProgressColor(@ColorInt progressColor:Int,@ColorInt progressBgColor: Int){
        paint.color=progressColor
        bgPaint.color=progressBgColor
        invalidate()
    }
}