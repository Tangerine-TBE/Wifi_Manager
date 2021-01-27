package com.feisukj.cleaning.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class BatteryView2 @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?=null, defStyle:Int=0):View(context,attributeSet,defStyle) {
    var batterQuantity=0.4f
        set(value) {
            if (value>1f){
                throw IllegalArgumentException("电池电量范围为0到1")
            }
            if (value!=field){
                field=value
                invalidate()
            }
        }

    private val anodeRectPaint=Paint().apply {
        this.color= Color.parseColor("#61697C")
        this.style=Paint.Style.FILL
        this.strokeWidth=resources.displayMetrics.density
        this.isAntiAlias=true
    }
    private val outRectPaint=Paint().apply {
        this.color=Color.parseColor("#61697C")
        this.style=Paint.Style.STROKE
        this.strokeWidth=resources.displayMetrics.density
        this.isAntiAlias=true
    }
    private val batteryBgPaint=Paint().apply {
        this.isAntiAlias=true
    }
    private val innerRectPaint=Paint().apply {
        this.color=Color.parseColor("#0000FFA8")
        this.style=Paint.Style.FILL
        this.isAntiAlias=true
    }

    private val stripRectPaint1=Paint().apply {
        this.color=Color.parseColor("#FAFAFA")
        this.style=Paint.Style.FILL
        this.isAntiAlias=true
    }
    private val stripRectPaint2=Paint().apply {
        this.color=Color.parseColor("#1731DE")
        this.style=Paint.Style.FILL
        this.isAntiAlias=true
    }
    private val path1 = Path()
    private val path2 = Path()
    private val path3 = Path()
    private val path4 = Path()
    private val path5 = Path()
    private val path6 = Path()

    private var left = 0f
    private var right = 0f
    private var top = 0f
    private var bottom = 0f
    private val dp = resources.displayMetrics.density

    private val anodeRectWidth=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10f,resources.displayMetrics)
    private val anodeRectHeight=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30f,resources.displayMetrics)
    private val anodeRect=RectF()

    private val outRect=RectF()
    private val innerRect=RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        anodeRect.apply {
            this.left=(width-anodeRectWidth)
            this.right=width.toFloat()-anodeRectPaint.strokeWidth/2
            this.top=(height-anodeRectHeight)/2
            this.bottom=(height+anodeRectHeight)/2
        }
        outRect.apply {
            this.left=0f+outRectPaint.strokeWidth/2
            this.right=width.toFloat()-outRectPaint.strokeWidth/2-anodeRect.width()
            this.top=0f+outRectPaint.strokeWidth/2
            this.bottom=height.toFloat()-outRectPaint.strokeWidth/2
        }

        val b=Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
        val c=Canvas(b)
        c.drawARGB(0x26,0xff,0xff,0xff)
        batteryBgPaint.shader=BitmapShader(b,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)

        innerRect.apply {
            this.left=0f+outRectPaint.strokeWidth+4*resources.displayMetrics.density

            this.right=outRect.left+4*resources.displayMetrics.density+(outRect.width()-8*resources.displayMetrics.density)*batterQuantity

            this.top=outRect.top+4*resources.displayMetrics.density+outRectPaint.strokeWidth/2

            this.bottom=outRect.bottom-outRectPaint.strokeWidth/2-4*resources.displayMetrics.density
        }

        left = 0f+outRectPaint.strokeWidth+4*dp
        right = this.left + (outRect.width()-8*dp-10*dp)/6
        top = outRect.top+4*dp+outRectPaint.strokeWidth/2
        bottom = outRect.bottom-outRectPaint.strokeWidth/2-4*dp

        path1.apply {
            this.moveTo(left,top)
            this.lineTo(left,bottom)
            this.lineTo(right-5*dp+10*dp,bottom)
            this.lineTo(right-5*dp,top)
            this.close()
        }
        path2.apply {
            this.moveTo(left+(right-left),top)
            this.lineTo(left+(right-left)+10*dp,bottom)
            this.lineTo(left+(right-left)+10*dp+1*(right-left-5*dp),bottom)
            this.lineTo(left+(right-left)+1*(right-left-5*dp),top)
        }
        path3.apply {
            this.moveTo(left+2*(right-left),top)
            this.lineTo(left+2*(right-left)+10*dp,bottom)
            this.lineTo(left+2*(right-left)+10*dp+(right-left-5*dp),bottom)
            this.lineTo(left+2*(right-left)+(right-left-5*dp),top)
        }
        path4.apply {
            this.moveTo(left+3*(right-left),top)
            this.lineTo(left+3*(right-left)+10*dp,bottom)
            this.lineTo(left+3*(right-left)+10*dp+(right-left-5*dp),bottom)
            this.lineTo(left+3*(right-left)+(right-left-5*dp),top)
        }
        path5.apply {
            this.moveTo(left+4*(right-left),top)
            this.lineTo(left+4*(right-left)+10*dp,bottom)
            this.lineTo(left+4*(right-left)+10*dp+(right-left-5*dp),bottom)
            this.lineTo(left+4*(right-left)+(right-left-5*dp),top)
        }
        path6.apply {
            this.moveTo(left+5*(right-left),top)
            this.lineTo(left+5*(right-left)+10*dp,bottom)
            this.lineTo(left+5*(right-left)+10*dp+(right-left-5*dp),bottom)
            this.lineTo(left+5*(right-left)+(right-left-5*dp)+10*dp,top)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        innerRect.right=outRect.left+4*resources.displayMetrics.density+(outRect.width()-8*resources.displayMetrics.density)*batterQuantity

        canvas?.drawRoundRect(anodeRect,resources.displayMetrics.density,resources.displayMetrics.density,batteryBgPaint)
        canvas?.drawRoundRect(anodeRect,resources.displayMetrics.density,resources.displayMetrics.density,anodeRectPaint)

        canvas?.drawRoundRect(outRect,resources.displayMetrics.density*8,resources.displayMetrics.density*8,batteryBgPaint)
        canvas?.drawRoundRect(outRect,resources.displayMetrics.density*8,resources.displayMetrics.density*8,outRectPaint)

        canvas?.drawRoundRect(innerRect,resources.displayMetrics.density*8,resources.displayMetrics.density*8,innerRectPaint)

        canvas?.drawPath(path1,stripRectPaint1)
        canvas?.drawPath(path2,stripRectPaint1)
        canvas?.drawPath(path3,stripRectPaint1)
        canvas?.drawPath(path4,stripRectPaint1)
        canvas?.drawPath(path5,stripRectPaint1)
        canvas?.drawPath(path6,stripRectPaint1)

        batterQuantity.also {
            when{
                it in 0f..(1f/6f) ->{

                }
                it in (1f/6f)..(2f/6f) ->{
                    canvas?.drawPath(path1,stripRectPaint2)
                }
                it in (2f/6f)..(3f/6f) ->{
                    canvas?.drawPath(path1,stripRectPaint2)
                    canvas?.drawPath(path2,stripRectPaint2)
                }
                it in (3f/6f)..(4f/6f) ->{
                    canvas?.drawPath(path1,stripRectPaint2)
                    canvas?.drawPath(path2,stripRectPaint2)
                    canvas?.drawPath(path3,stripRectPaint2)
                }
                it in (4f/6f)..(5f/6f) ->{
                    canvas?.drawPath(path1,stripRectPaint2)
                    canvas?.drawPath(path2,stripRectPaint2)
                    canvas?.drawPath(path3,stripRectPaint2)
                    canvas?.drawPath(path4,stripRectPaint2)
                }
                it in (5f/6f)..0.999f ->{
                    canvas?.drawPath(path1,stripRectPaint2)
                    canvas?.drawPath(path2,stripRectPaint2)
                    canvas?.drawPath(path3,stripRectPaint2)
                    canvas?.drawPath(path4,stripRectPaint2)
                    canvas?.drawPath(path5,stripRectPaint2)
                }
                else ->{
                    canvas?.drawPath(path1,stripRectPaint2)
                    canvas?.drawPath(path2,stripRectPaint2)
                    canvas?.drawPath(path3,stripRectPaint2)
                    canvas?.drawPath(path4,stripRectPaint2)
                    canvas?.drawPath(path5,stripRectPaint2)
                    canvas?.drawPath(path6,stripRectPaint2)
                }
            }
        }
    }
}