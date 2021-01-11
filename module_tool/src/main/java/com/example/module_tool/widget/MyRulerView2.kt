package com.example.module_tool.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.example.module_tool.base.BaseConstant
import com.example.module_tool.utils.ColorUtil
import java.text.NumberFormat
import kotlin.math.abs

/**
 * @author yf
 * QQ:1446406982
 */
class MyRulerView2 @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defStyleAttr:Int=0):View(context,attributeSet,defStyleAttr){
    companion object{
        private val nf:NumberFormat= NumberFormat.getNumberInstance().also {
            it.minimumFractionDigits=2
            it.maximumFractionDigits=2
        }
        //1毫米所占的像素个数
        private var unitMM=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,1f, BaseConstant.application.resources.displayMetrics)
        //1英寸所占的像素个数
        private var unitIN=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN,1f,BaseConstant.application.resources.displayMetrics)

        private val unitDP=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1f,BaseConstant.application.resources.displayMetrics)
    }
    enum class LengthUnit(initPx:Float){
        mm(unitMM),cm(unitMM),inch(unitIN/16),inchPer(unitIN/16);
        var px:Float=0f
            private set
        val carry by lazy { if (this==inch||this==inchPer) 16 else 10 }
        init {
            px=initPx
        }
        companion object{
            /**
             * 校对的方法
             * @param pxInCm 单位厘米中具有的像素数
             */
            fun proofread(pxInCm:Float){
                val zoom=pxInCm/cm.px/10
                values().forEach {
                    it.px*=zoom
                }
            }
        }
        fun valueToString(length:Float):String{
            return when(this){
                mm->{
                    nf.minimumFractionDigits=1
                    nf.maximumFractionDigits=1
                    nf.format(length/px)+"mm"
                }
                cm -> {
                    nf.minimumFractionDigits=2
                    nf.maximumFractionDigits=2
                    nf.format(length/px/carry)+"cm"
                }
                inch -> {
                    nf.format(length/px/carry)+"inch"
                }
                inchPer->{
                    val integer=(length/px/carry+0.5).toInt()
                    var remainder=(length/px+0.5).toInt()%carry
                    var denominator=carry
                    while (remainder%2==0&&remainder!=0){
                        remainder=remainder.shr(1)
                        denominator=denominator.shr(1)
                    }
                    if (remainder!=0) {
                        if (integer!=0) {
                            "$integer  $remainder/$denominator inch"
                        }else{
                            "$remainder/$denominator inch"
                        }
                    }else{
                        integer.toString()
                    }
                }
            }
        }
    }

    /**
     * 单位
     */
    var unit:LengthUnit=LengthUnit.cm
    private val textBounds:Rect by lazy { Rect() }
    //当前所指向的横向刻度
    private var currentScaleX:Float=0f
    //当前所指向的纵向刻度
    private var currentScaleY:Float=0f
    //尺子的刻度画笔
    private val rulerScalePaint=Paint().apply {
        color= Color.parseColor(ColorUtil.COLOR_theme)
        isAntiAlias=true
        style=Paint.Style.STROKE
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
    //用来移动的直线的画笔
    private val moveLinePaint=Paint().apply {
        color= Color.parseColor(ColorUtil.COLOR_theme)
        isAntiAlias=true
        style=Paint.Style.STROKE
        strokeWidth=2f
    }
    //移动的直线下面的圆的画笔
    private val circlePaint=Paint().apply {
        color= Color.parseColor(ColorUtil.COLOR_theme)
        isAntiAlias=true
        style=Paint.Style.FILL
        strokeWidth=2f
    }
    //结果画笔
    private val resultPaint=Paint().apply {
        color= Color.WHITE
        isAntiAlias=true
        style=Paint.Style.FILL
        strokeWidth=2f
        textSize=40f
    }
    private var scaleCountL=0//直尺的长度
    private var scaleCountH=0
    var isYScaleRight=true//y轴是否在右边；设置为true时纵向尺子靠右边，默认左边
        set(value) {
            if (value!=field){
                invalidate()
                field=value
            }
        }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentScaleX=w/2f
        currentScaleY=h/2f
        scaleCountL=(width/unit.px).toInt()
        scaleCountH=(height/unit.px).toInt()
    }

    override fun invalidate() {
        super.invalidate()
        scaleCountL=(width/unit.px).toInt()
        scaleCountH=(height/unit.px).toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawRuler(canvas)
        drawLine(canvas)
    }

    private fun getEndX_Y(scaleLength:Float, per:Float):Float{
        return if (isYScaleRight) width-per*scaleLength else scaleLength*per
    }
    
    private fun getTextX_Y(scaleLength: Float, textMargin:Float, per: Float, textWidth:Int):Float{
        return if (isYScaleRight) width-scaleLength*per-textMargin-textWidth else scaleLength*per+textMargin
    }
    /**
     * 画尺子（刻度和分数,16进制）
     */
    private fun drawRuler(canvas: Canvas?){
        val textMargin=unit.px*1.5f//刻度标注的字距离刻度的距离
        val scaleLength=unit.px*3f//最长刻度长度
        //画数字0
        val t="0"
        rulerNumPaint.getTextBounds(t, 0, t.length, textBounds)
        val x=if (isYScaleRight) width-textBounds.width()*0.6f-unit.px*2 else 0 - textBounds.width() * 0.6f+unit.px*2
        val y=0 + textBounds.height() * 0.6f+unit.px*2
        canvas?.drawText(t, x, y, rulerNumPaint)
        //画横向尺子
        for (i in 0 until scaleCountL+1){
            if(i in 1..4){
                continue
            }
            val scaleX=if (isYScaleRight) width-i*unit.px else i*unit.px
            if (i%unit.carry==0){
                canvas?.drawLine(scaleX,0f,scaleX,scaleLength,rulerScalePaint)
                if (i!=0) {
                    val text = (i / unit.carry).toString()
                    rulerNumPaint.getTextBounds(text, 0, text.length, textBounds)
                    canvas?.drawText(text, scaleX - textBounds.width() * 0.6f, scaleLength + textMargin+textBounds.height(), rulerNumPaint)
                }
            }else if (i%(unit.carry/2)==0){
                canvas?.drawLine(scaleX,0f,scaleX,scaleLength*0.7f,rulerScalePaint)

                if (unit==LengthUnit.inchPer){
                    val text = if (i<unit.carry){
                        "½"
                    }else{
                        (i / unit.carry).toString()+"½"
                    }
                    rulerNumPaint.getTextBounds(text, 0, text.length, textBounds)
                    canvas?.drawText(text, scaleX - textBounds.width() * 0.6f, scaleLength + textMargin+textBounds.height()*0.7f, rulerNumPaint)
                }
            }else if (unit.carry==LengthUnit.inch.carry&&i%(unit.carry/4)==0){
                canvas?.drawLine(scaleX,0f,scaleX,scaleLength*0.65f,rulerScalePaint)

                if (unit==LengthUnit.inchPer){
                    val remainder=i%unit.carry
                    val per=if (remainder%(unit.carry*0.75f).toInt()==0){
                        "¾"
                    }else{
                        "¼"
                    }
                    val text=if (i<unit.carry){
                        per
                    }else{
                        (i / unit.carry).toString()+per
                    }
                    rulerNumPaint.getTextBounds(text, 0, text.length, textBounds)
                    canvas?.drawText(text, scaleX - textBounds.width() * 0.6f, scaleLength + textMargin+textBounds.height()*0.7f, rulerNumPaint)
                }
            }else{
                canvas?.drawLine(scaleX,0f,scaleX,scaleLength*0.5f,rulerScalePaint)
            }
        }
        //画纵向尺子
        for (i in 0 until scaleCountH+1){
            if(i in 1..4){
                continue
            }
            val scaleY=i*unit.px

            val startX=if(!isYScaleRight) 0f else width.toFloat()
            if (i%unit.carry==0){
                canvas?.drawLine(startX,scaleY,getEndX_Y(scaleLength,1f),scaleY,rulerScalePaint)
                if (i!=0) {
                    val text = (i / unit.carry).toString()
                    rulerNumPaint.getTextBounds(text, 0, text.length, textBounds)
//                    canvas?.drawText(text, scaleLength + textMargin, scaleY + textBounds.height() * 0.6f, rulerNumPaint)
                    canvas?.drawText(text, getTextX_Y(scaleLength,textMargin,1f,textBounds.width()), scaleY + textBounds.height() * 0.6f, rulerNumPaint)
                }
            }else if (i%(unit.carry/2)==0){
                canvas?.drawLine(startX,scaleY,getEndX_Y(scaleLength,0.7f),scaleY,rulerScalePaint)

                if (unit==LengthUnit.inchPer){
                    val text = if (i<unit.carry){
                        "½"
                    }else{
                        (i / unit.carry).toString()+"½"
                    }
                    rulerNumPaint.getTextBounds(text, 0, text.length, textBounds)
                    canvas?.drawText(text, getTextX_Y(scaleLength,textMargin,0.7f,textBounds.width()), scaleY + textBounds.height() * 0.6f, rulerNumPaint)
                }
            }else if (unit.carry==LengthUnit.inch.carry&&i%(unit.carry/4)==0){
                canvas?.drawLine(startX,scaleY,getEndX_Y(scaleLength,0.65f),scaleY,rulerScalePaint)

                if (unit==LengthUnit.inchPer){
                    val remainder=i%unit.carry
                    val per=if (remainder%(unit.carry*0.75f).toInt()==0){
                        "¾"
                    }else{
                        "¼"
                    }
                    val text=if (i<unit.carry){
                        per
                    }else{
                        (i / unit.carry).toString()+per
                    }
                    rulerNumPaint.getTextBounds(text, 0, text.length, textBounds)
                    canvas?.drawText(text, getTextX_Y(scaleLength,textMargin,0.7f,textBounds.width()), scaleY + textBounds.height() * 0.6f, rulerNumPaint)
                }
            }else{
                canvas?.drawLine(startX,scaleY,getEndX_Y(scaleLength,0.5f),scaleY,rulerScalePaint)
            }
        }
    }

    /**
     * 画直线及直线对应的结果
     */
    private val r=unitDP*44
    private fun drawLine(canvas: Canvas?){
        //横向刻度的直线
        canvas?.drawLine(currentScaleX,0f,currentScaleX,height.toFloat(),moveLinePaint)
        canvas?.drawCircle(currentScaleX,height.toFloat(), r,circlePaint)
        val textX=unit.valueToString(if (isYScaleRight) width-currentScaleX else currentScaleX)
        resultPaint.getTextBounds(textX,0,textX.length,textBounds)
        canvas?.drawText(textX,currentScaleX-textBounds.width()*0.5f,height-r*0.5f+textBounds.height()*0.6f,resultPaint)
        //纵向刻度的直线
        canvas?.drawLine(0f,currentScaleY,width.toFloat(),currentScaleY,moveLinePaint)
        canvas?.drawCircle(if (isYScaleRight) 0f else width.toFloat(),currentScaleY, r,circlePaint)
        val textY=unit.valueToString(currentScaleY)
        resultPaint.getTextBounds(textY,0,textY.length,textBounds)
        val x=if (isYScaleRight) r*0.5f-textBounds.width()*0.5f else width-r*0.5f-textBounds.width()*0.5f
        val y=currentScaleY+textBounds.height()*0.5f
        canvas?.save()
        canvas?.rotate(-90f*(if (isYScaleRight) -1 else 1),if (isYScaleRight) r*0.5f else width-r*0.5f,currentScaleY)
        canvas?.drawText(textY,x,y,resultPaint)
        canvas?.restore()
    }

    private enum class TouchLine{
        lineX,lineY,noTouch
    }
    private var touchLine=TouchLine.noTouch
    private var lastX=0f
    private var lastY=0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?:return super.onTouchEvent(event)
        val action= event.action
        val x= event.x
        val y= event.y
        when(action){
            MotionEvent.ACTION_DOWN,MotionEvent.ACTION_UP->{
                val distanceX=abs(currentScaleX-x)
                val distanceY=abs(currentScaleY-y)
                if (distanceX<r||distanceY<r){
                    if (distanceX<=distanceY){
                        touchLine=TouchLine.lineX
                    }else{
                        touchLine=TouchLine.lineY
                    }
                    lastX=x
                    lastY=y
                }else{
                    touchLine=TouchLine.noTouch
                }
            }
            MotionEvent.ACTION_MOVE->{
                if (touchLine==TouchLine.lineX){
                    currentScaleX+=(x-lastX)
                    if (currentScaleX<=0){
                        currentScaleX=0f
                    }else if(currentScaleX>width){
                        currentScaleX=width.toFloat()
                    }
                    invalidate()
                }else{
                    currentScaleY+=(y-lastY)
                    if (currentScaleY<=0){
                        currentScaleY=0f
                    }else if (currentScaleY>height){
                        currentScaleY=height.toFloat()
                    }
                    invalidate()
                }
                lastX=x
                lastY=y
            }
            else->{
                touchLine=TouchLine.noTouch
            }
        }
        if (touchLine!=TouchLine.noTouch){
            return true
        }else{
            return super.onTouchEvent(event)
        }
    }
}