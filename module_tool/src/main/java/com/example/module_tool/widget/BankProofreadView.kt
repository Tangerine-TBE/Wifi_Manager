package com.example.module_tool.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.module_tool.base.BaseConstant
import kotlin.math.abs

val mm= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,1f, BaseConstant.application.resources.displayMetrics)
class BankProofreadView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?=null, defStyle:Int=0): View(context,attributeSet,defStyle) {
    companion object{
        private const val BANK_CARD_WIDTH=85.60f//银行卡长
        private const val BANK_CARD_HEIGHT=53.98f//银行卡宽
    }
    var isWidth=true
    private var rectOutR=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,22f, BaseConstant.application.resources.displayMetrics)
    private val rectHigh=mm*10
    private var startLocation=0f
    private var endLocation=0f
    private val minDis=mm*BANK_CARD_HEIGHT/2
    
    private lateinit var rect:RectF

    //矩形画笔
    private val rectPaint= Paint().apply {
        color= Color.parseColor("#FF92BFFF")
        isAntiAlias=true
        style= Paint.Style.FILL
    }
    //圆的画笔
    private val circlePaint= Paint().apply {
        color= Color.parseColor("#FF0068FF")
        isAntiAlias=true
        style= Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startLocation=(width-BANK_CARD_WIDTH* mm)/2
        endLocation=startLocation+BANK_CARD_WIDTH* mm
        
        rect=RectF(startLocation,0f,endLocation,rectHigh)
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rect.left=startLocation
        rect.right=endLocation
        canvas?.drawRoundRect(rect,8f,8f,rectPaint)
        canvas?.drawCircle(startLocation,rectHigh,rectOutR,circlePaint)
        canvas?.drawCircle(endLocation,rectHigh,rectOutR,circlePaint)
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
                if (lastY<rectHigh+rectOutR) {
                    val startDis = abs(startLocation - lastX)
                    val endDis = abs(endLocation - lastX)
                    if (startDis < rectOutR || endDis < rectOutR) {
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
        return if (isWidth){
            length/ BANK_CARD_WIDTH
        }else{
            length/ BANK_CARD_HEIGHT
        } * 10
    }
}