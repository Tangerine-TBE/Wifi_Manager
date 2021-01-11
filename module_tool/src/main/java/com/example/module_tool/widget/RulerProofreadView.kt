package com.example.module_tool.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.module_tool.utils.ColorUtil
import kotlin.math.abs

class RulerProofreadView @JvmOverloads constructor(context:Context,attributeSet: AttributeSet?=null,defStyle:Int=0):View(context,attributeSet,defStyle){
    private val bounds:Rect by lazy { Rect() }
    private var numOutR=mm *3
    private var startLocation=0f
    private var endLocation=0f
    private val minDis= mm * 10
    var unit= mm

    //尺子的刻度画笔
    private val rulerScalePaint= Paint().apply {
        color= Color.parseColor(ColorUtil.COLOR_theme)
        isAntiAlias=true
        style= Paint.Style.STROKE
        strokeWidth=2f
    }
    //尺子的数字画笔
    private val rulerNumPaint=Paint().apply {
        color= Color.parseColor(ColorUtil.COLOR_theme)
        isAntiAlias=true
        style=Paint.Style.FILL
        strokeWidth=2f
        textSize=48f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startLocation=(width- mm * 20)/2
        endLocation=startLocation+mm *20
    }

    private val scaleLength= mm *5
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var startX=startLocation
        unit=(endLocation-startLocation)/20

        var i=0
        while (i<=20){
            if (i%10==0){
                scaleLength*1
                //画刻度
                canvas?.drawLine(startX,0f,startX,scaleLength,rulerScalePaint)
                //画数字
                val text=(i/10).toString()
                rulerNumPaint.getTextBounds(text,0,text.length,bounds)
                canvas?.drawText(text,startX-bounds.width()/2,scaleLength+bounds.height()/2+numOutR,rulerNumPaint)

                if (i==0||i==20) {
                    canvas?.drawCircle(startX, scaleLength + numOutR, numOutR, rulerScalePaint)
                }
            }else if (i%5==0){
                //画刻度
                canvas?.drawLine(startX,0f,startX,scaleLength*0.7f,rulerScalePaint)
            }else{
                //画刻度
                canvas?.drawLine(startX,0f,startX,scaleLength*0.5f,rulerScalePaint)
            }
            startX+=unit
            i++
        }
    }

    private enum class TouchPoint{
        startPoint,endPoint,noTouch
    }
    private var touchPoint=TouchPoint.noTouch
    private var lastX=0f
    private var lastY=0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?:return super.onTouchEvent(event)
        val action=event.action
        when(action){
            MotionEvent.ACTION_DOWN->{
                lastX=event.x
                lastY=event.y
                if (lastY<scaleLength+numOutR*2) {
                    val startDis = abs(startLocation - lastX)
                    val endDis = abs(endLocation - lastX)
                    if (startDis < numOutR || endDis < numOutR) {
                        touchPoint = if (startDis < endDis) {
                            TouchPoint.startPoint
                        }else{
                            TouchPoint.endPoint
                        }
                    }else{
                        touchPoint=TouchPoint.noTouch
                    }
                }else{
                    touchPoint=TouchPoint.noTouch
                }
            }
            MotionEvent.ACTION_MOVE->{
                if (touchPoint==TouchPoint.startPoint){
                    startLocation+=(event.x-lastX)
                    if (endLocation-startLocation<minDis){
                        startLocation=endLocation-minDis
                    }
                    invalidate()
                }else if (touchPoint==TouchPoint.endPoint){
                    endLocation+=(event.x-lastX)
                    if (endLocation-startLocation<minDis){
                        endLocation=startLocation+minDis
                    }
                    invalidate()
                }
                lastX=event.x
            }
            else->{
                touchPoint=TouchPoint.noTouch
            }
        }
        if (touchPoint==TouchPoint.noTouch){
            return super.onTouchEvent(event)
        }else{
            return true
        }
    }

    fun getUnitCMLength():Float{
        val length=endLocation-startLocation
        return length/2
    }
}