package com.feisukj.cleaning.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.ColorInt
import android.util.AttributeSet
import android.view.View

class SmallLoading @JvmOverloads constructor(context:Context, attrs:AttributeSet?=null): View(context,attrs) {
    @ColorInt
    var background:Int=Color.parseColor("#ffffff")
    private val backgroundPaint= Paint(Paint.ANTI_ALIAS_FLAG)

    @ColorInt
    var defaultColor:Int=Color.parseColor("#737373")
    private val defaultPaint= Paint(Paint.ANTI_ALIAS_FLAG)
    @ColorInt
    var currentcolor:Int=Color.parseColor("#0EB0D7")
    private val currentPaint=Paint(Paint.ANTI_ALIAS_FLAG)
    init {
        backgroundPaint.style=Paint.Style.FILL
        backgroundPaint.color=background

        defaultPaint.style=Paint.Style.FILL
        defaultPaint.color=defaultColor

        currentPaint.style=Paint.Style.FILL
        currentPaint.color=currentcolor

    }
    var radius=0F
    private var rectF:RectF?=null
    private fun initData(){
        if (radius==0F){
            radius=Math.min(width,height).toFloat()/2
        }
        rectF= RectF(width/2-radius,height/2-radius,width/2+radius,height/2+radius)
    }

    override fun onDraw(canvas: Canvas?) {
        if (radius==0F||rectF==null) {
            initData()
        }
        super.onDraw(canvas)
        for (j in 0 until count){
            val pair=getAngle(j,count)
            if (j!=i) {
                canvas?.drawArc(rectF!!, pair.first, pair.second, true, defaultPaint)
            }else{
                canvas?.drawArc(rectF!!, pair.first, pair.second, true, currentPaint)
            }
        }
        canvas?.drawCircle(width/2.toFloat(),height/2.toFloat(),radius*0.8F,backgroundPaint)
    }
    private var i=0
    private val count=8
    var isStop=false

    fun startAnim(){
        isStop=false
        Thread{
            kotlin.run {
                while (!isStop) {
                    i++
                    if (i >= count) {
                        i = 0
                    }
                    Thread.sleep(200)
                    post { invalidate() }
                }
            }
        }.start()
    }

    private fun getAngle(index:Int,count:Int):Pair<Float,Float>{
        val pair:Pair<Float,Float>
        val angle=360/count.toFloat()
        val startAngle=angle*index+angle*0.15F
        val sweepAngle=angle*0.7F
        pair=Pair(startAngle,sweepAngle)
        return pair
    }

    fun stopAnim(){
        isStop=true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isStop=false
    }
}