package com.example.module_tool.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.module_tool.R
import com.example.module_tool.base.BaseConstant
import kotlin.math.abs

/**
 * 1美分：直径19.05mm，厚度1.55mm，重量2.5g
5美分：直径21.21mm，厚度1.95mm，重量5.0g
10美分：直径17.91mm，厚度1.35mm，重量2.268g
25美分：直径24.26mm，厚度1.75mm，重量5.67g
50美分：直径30.61mm，厚度2.15mm，重量11.34g
老1美元：直径38.1mm，厚度2.58mm，重量22.68g
新1美元：直径26.5mm，厚度2.0mm，重量8.1g
 */
enum class CoinSize(val size:Float,val des:String,private val kind: Kind){
    RMB_0_1(19f, BaseConstant.application.getString(R.string.rmb_1),Kind.RMB),
    RMB_0_5(20.5f,BaseConstant.application.getString(R.string.rmb_5),Kind.RMB),
    RMB_1_0(25f,BaseConstant.application.getString(R.string.rmb_10),Kind.RMB),

    USA001(19.05f,BaseConstant.application.getString(R.string.usa_1),Kind.USA),
    USA005(21.21f,BaseConstant.application.getString(R.string.usa_5),Kind.USA),
    USA010(17.91f,BaseConstant.application.getString(R.string.usa_10),Kind.USA),
    USA025(24.26f,BaseConstant.application.getString(R.string.usa_25),Kind.USA),
    USA050(30.61f,BaseConstant.application.getString(R.string.usa_50),Kind.USA),
    USA100_Old(38.1f,BaseConstant.application.getString(R.string.usa_100_old),Kind.USA),
    USA100_New(26.5f,BaseConstant.application.getString(R.string.usa_100_new),Kind.USA);
    companion object{
        enum class Kind(){
            RMB,USA
        }
        fun getCoinTypes(kind:Kind):List<CoinSize>{
            return CoinSize.values().filter {
                it.kind==kind
            }
        }
    }
}
class CoinProofreadView @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defStyle:Int=0):View(context,attributeSet,defStyle) {
    private var leftTopPointX=0f
    private var rightBottomPointX=0f
    private var leftTopPointY=0f
    private var rightBottomPointY=0f
    private var circlePointR=mm*3
    /*****************************画笔********************************/
    //虚线画笔
    private val dottedLinePaint= Paint().apply {
        color= Color.parseColor("#FF0068FF")
        isAntiAlias=true
        style= Paint.Style.STROKE
        strokeWidth=2f
    }
    //圆点画笔
    private val circlePointPaint= Paint().apply {
        color= Color.parseColor("#FF0068FF")
        isAntiAlias=true
        style= Paint.Style.FILL
        strokeWidth=2f
    }
    //圆画笔
    private val circlePaint= Paint().apply {
        color= Color.parseColor("#FF92BFFF")
        isAntiAlias=true
        style= Paint.Style.FILL
    }
    /*****************************画笔End********************************/
    private val minCircel=CoinSize.RMB_0_1.size*0.8f* mm
    var currentCoin=CoinSize.RMB_0_5
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val size=currentCoin.size* mm
        leftTopPointX=(w-size)/2
        rightBottomPointX=leftTopPointX+size
        leftTopPointY=(h-size)/2
        rightBottomPointY=leftTopPointY+size
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val circleX=(rightBottomPointX-leftTopPointX)/2+leftTopPointX
        val circleY=(rightBottomPointY-leftTopPointY)/2+leftTopPointY
        //两个圆点
        canvas?.drawCircle(leftTopPointX,leftTopPointY,circlePointR,circlePointPaint)
        canvas?.drawCircle(rightBottomPointX,rightBottomPointY,circlePointR,circlePointPaint)
        //四条边
        val count=((rightBottomPointX-leftTopPointX)/mm/2).toInt()
        var startX=leftTopPointX
        var startY=leftTopPointY
        for (i in 0 until count){
            canvas?.drawLine(startX,leftTopPointY,startX+ mm,leftTopPointY,dottedLinePaint)
            canvas?.drawLine(startX,rightBottomPointY,startX+ mm,rightBottomPointY,dottedLinePaint)
            startX+= mm*2
            canvas?.drawLine(leftTopPointX,startY,leftTopPointX,startY+ mm,dottedLinePaint)
            canvas?.drawLine(rightBottomPointX,startY,rightBottomPointX,startY+ mm,dottedLinePaint)
            startY+=2*mm
        }
        //中间的大圆（硬币）
        canvas?.drawCircle(circleX,circleY,(rightBottomPointX-leftTopPointX)/2,circlePaint)
    }

    private enum class TouchPoint{
        touchLeftTop,touchRightBottom,noTouch
    }
    private var touchPoint=TouchPoint.noTouch
    private var lastX=0f
    private var lastY=0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?:return super.onTouchEvent(event)
        val action= event.action
        val x= event.x
        val y= event.y
        when(action){
            MotionEvent.ACTION_DOWN->{
                val leftTopDisX= abs(leftTopPointX-x)
                val leftTopDisY= abs(leftTopPointY-y)
                val rightBottomDisX= abs(rightBottomPointX-x)
                val rightBottomDisY= abs(rightBottomPointY-y)
                if (leftTopDisX<circlePointR&&leftTopDisY<circlePointR){
                    touchPoint=TouchPoint.touchLeftTop
                }else if (rightBottomDisX<circlePointR&&rightBottomDisY<circlePointR){
                    touchPoint=TouchPoint.touchRightBottom
                }else{
                    touchPoint=TouchPoint.noTouch
                }
                lastX=x
                lastY=y
            }
            MotionEvent.ACTION_MOVE->{
                val disX=x-lastX
                val disY=y-lastY
                val add=if (abs(disX)> abs(disY)) disX else disY
                if (touchPoint==TouchPoint.touchLeftTop){
                    leftTopPointX+=add
                    leftTopPointY+=add
                    if (leftTopPointX<=0||leftTopPointY<=0){
                        leftTopPointX-=add
                        leftTopPointY-=add
                    }else if (rightBottomPointX-leftTopPointX<minCircel||rightBottomPointY-leftTopPointY<minCircel){
                        leftTopPointX-=add
                        leftTopPointY-=add
                    }
                    invalidate()
                }else if (touchPoint==TouchPoint.touchRightBottom){
                    rightBottomPointX+=add
                    rightBottomPointY+=add
                    if (rightBottomPointX+mm>=width||rightBottomPointY+mm>=height){
                        rightBottomPointX-=add
                        rightBottomPointY-=add
                    }else if (rightBottomPointX-leftTopPointX<minCircel||rightBottomPointY-leftTopPointY<minCircel){
                        rightBottomPointX-=add
                        rightBottomPointY-=add
                    }
                    invalidate()
                }
                lastX=x
                lastY=y
            }
            else->{
                touchPoint=TouchPoint.noTouch
            }
        }
        if (touchPoint!=TouchPoint.noTouch){
            return true
        }else{
            return super.onTouchEvent(event)
        }
    }

    fun getUnitCMLength():Float{
        val length=rightBottomPointX-leftTopPointX
        return length/currentCoin.size*10
    }
}