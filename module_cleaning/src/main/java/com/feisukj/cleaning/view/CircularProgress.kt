package com.feisukj.cleaning.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.feisukj.cleaning.R
import java.text.DecimalFormat

class CircularProgress @JvmOverloads constructor (context:Context,attrs: AttributeSet?=null,defStyleAttr:Int=0):View(context,attrs,defStyleAttr) {
    var progress:Float=0F
        set(value) {
            val anim=ValueAnimator.ofFloat(0F,value)
            anim.duration=(value*9000F).toLong()
            anim.addUpdateListener {
                field=it.animatedValue as Float
                invalidate()
            }
            anim.start()
        }
    var defOutProgressColor:Int=0
    var fillOutProgressColor:Int=0
    var insideColor:Int=0
    var centerText:String=""
    var leftToRight:Boolean=true
    var radius:Float=0F
    var progressTextSize:Int=0
    var progressTextColor:Int=0
    init {
        val typedArray=context.obtainStyledAttributes(attrs, R.styleable.CircularProgress)
        progress=typedArray.getFloat(R.styleable.CircularProgress_progress,0F)
        defOutProgressColor=typedArray.getColor(R.styleable.CircularProgress_defOutProgressColor,Color.RED)
        fillOutProgressColor=typedArray.getColor(R.styleable.CircularProgress_fillOutProgressColor,Color.BLUE)
        insideColor=typedArray.getColor(R.styleable.CircularProgress_insideColor,Color.WHITE)
        centerText=typedArray.getString(R.styleable.CircularProgress_centerText)?:""
        leftToRight=typedArray.getBoolean(R.styleable.CircularProgress_leftToRight,true)
        radius=typedArray.getFloat(R.styleable.CircularProgress_radio,0F)
        progressTextColor=typedArray.getColor(R.styleable.CircularProgress_progressTextColor,Color.BLACK)
        progressTextSize=typedArray.getDimensionPixelSize(R.styleable.CircularProgress_progressTextSize,0)
        typedArray.recycle()
    }
    //画笔
    private val defOutProgressPaint=Paint(Paint.ANTI_ALIAS_FLAG)
    private val fillOutProgressPaint=Paint(Paint.ANTI_ALIAS_FLAG)
    private val insidePaint=Paint(Paint.ANTI_ALIAS_FLAG)
    private val centerTextPaint=Paint(Paint.ANTI_ALIAS_FLAG)

    private fun initPaint(){
        //最外圆
        defOutProgressPaint.style=Paint.Style.FILL
        defOutProgressPaint.color=defOutProgressColor

        //内圆
        insidePaint.style=Paint.Style.FILL
        insidePaint.color=insideColor

        //文字
        centerTextPaint.style=Paint.Style.STROKE
        if (progressTextColor!=0)
            centerTextPaint.color=progressTextColor
        if (progressTextSize!=0)
            centerTextPaint.textSize=progressTextSize.toFloat()

        //进度条画笔
        fillOutProgressPaint.style=Paint.Style.FILL
        fillOutProgressPaint.color=fillOutProgressColor
//        fillOutProgressPaint.strokeWidth =50F //设置画笔线条宽度
//        fillOutProgressPaint.strokeCap = Paint.Cap.ROUND//设置线条两端为圆形
    }
    private var rectF:RectF?=null
    private var text:String=""
    private val mText=Rect()
    private fun initData(){
        if (radius==0F){
            radius=Math.min(width,height).toFloat()/2
        }
        rectF=RectF(width/2-radius,height/2-radius,width/2+radius,height/2+radius)
        val format= DecimalFormat("#")
        text=format.format(progress*100)+"%"

    }
    override fun onDraw(canvas: Canvas?) {
        initPaint()
        initData()
        super.onDraw(canvas)
        //外圆
        canvas?.drawCircle((width/2F),(height/2F),radius,defOutProgressPaint)
        //圆弧
        if (rectF!=null) {
            canvas?.drawArc(rectF!!, 270F-progress*360, progress*360, true, fillOutProgressPaint)
        }
        //内圆
        canvas?.drawCircle((width/2F),(height/2F),radius*0.8F,insidePaint)
        //文字
        centerTextPaint.getTextBounds(text,0,text.length,mText)
        canvas?.drawText(text, width / 2 - mText.width() / 2F
                , height / 2 + mText.height() / 2F, centerTextPaint)
    }
}