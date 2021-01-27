package com.feisukj.cleaning.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.feisukj.cleaning.R
import java.util.*
import kotlin.collections.ArrayList

class ScanAnimator @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defSty:Int=0):View(context,attributeSet,defSty){
    private val framePaint=Paint().apply {
        this.style=Paint.Style.STROKE
        this.strokeWidth=3*resources.displayMetrics.density
        this.isAntiAlias=true
        this.color= Color.WHITE
        this.pathEffect=CornerPathEffect(strokeWidth)
    }
    private val framePath=Path()

    private val innerPaint=Paint().apply {
        this.style=Paint.Style.FILL
    }
    private val innerPath=Path()

    private val progressLinePaint=Paint().apply {
        this.color=ContextCompat.getColor(context,R.color.scan_animator_start)
    }
    private val progressLinePath=Path()
    private lateinit var linearGradient:LinearGradient
    var progress=0f

    private val lineWidth=resources.displayMetrics.density

    var listBitmap:List<Bitmap>?=null
        set(value) {
            if (value!=field){
                field=value
                updatePaintShader()
            }
        }
    private val bitmapRect=Rect()
    private val bitmapDrawRect=Rect()
    private val bitmapPaint=Paint()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val left=framePaint.strokeWidth/2
        val top=framePaint.strokeWidth/2
        val right=w-framePaint.strokeWidth/2
        val bottom=h-framePaint.strokeWidth/2
        framePath.moveTo((left+right)/2,top)
        framePath.lineTo(right,(top+bottom)/4)
        framePath.lineTo(right,(top+bottom)/4*3)
        framePath.lineTo((left+right)/2,bottom)
        framePath.lineTo(left,(top+bottom)/4*3)
        framePath.lineTo(left,(top+bottom)/4)
        framePath.close()

        val colors=IntArray(3){
            if (it%2==0){
                ContextCompat.getColor(context, R.color.scan_animator_start)
            }else{
                ContextCompat.getColor(context, R.color.scan_animator_end)
            }
        }
        linearGradient= LinearGradient(0f,0f,0f,h.toFloat(),colors,null,Shader.TileMode.CLAMP)
        innerPaint.shader=linearGradient

        bitmapDrawRect.also {
            it.left=left.toInt()
            it.top=((top+bottom)/4).toInt()
            it.right=right.toInt()
            it.bottom=((top+bottom)/4).toInt()*3
        }
        bitmapRect.also {
            it.left=left.toInt()
            it.top=((top+bottom)/4).toInt()
            it.right=right.toInt()
            it.bottom=((top+bottom)/4).toInt()*3
        }
        updatePaintShader()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return

        val progressHeight=height.toFloat()*progress

        if (bitmapPaint.shader!=null) {
            bitmapDrawRect.bottom =
                    when {
                        progressHeight <= bitmapDrawRect.top -> {
                            bitmapDrawRect.top
                        }
                        progressHeight <= bitmapDrawRect.top * 3 -> {
                            progressHeight.toInt()
                        }
                        else -> {
                            bitmapDrawRect.top * 3
                        }
                    }
            canvas.drawRect(bitmapDrawRect, bitmapPaint)
        }

        innerPath.reset()
        innerPath.lineTo(width.toFloat(),0f)
        innerPath.lineTo(width.toFloat(),progressHeight)
        innerPath.lineTo(0f,progressHeight)
        innerPath.close()
        innerPath.op(framePath,Path.Op.INTERSECT)
        canvas.drawPath(innerPath,innerPaint)

        progressLinePath.reset()
        progressLinePath.moveTo(0f,progressHeight-lineWidth)
        progressLinePath.lineTo(width.toFloat(),progressHeight-lineWidth)
        progressLinePath.lineTo(width.toFloat(),progressHeight)
        progressLinePath.lineTo(0f,progressHeight)
        progressLinePath.close()
        progressLinePath.op(framePath,Path.Op.INTERSECT)
        canvas.drawPath(progressLinePath,progressLinePaint)

        canvas.drawPath(framePath,framePaint)
    }

    private fun updatePaintShader(){
        val listBitmap=listBitmap?:return
        if (width==0||height==0){
            return
        }
        val bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val bitmapDrawW=bitmapRect.width()
        val bitmapDrawH=bitmapRect.height()
        val random=Random()
        val c=Canvas(bitmap)
        val paint=Paint()
        val matrix=Matrix()
        val xRange=ArrayList<IntRange>()
        val yRange=ArrayList<IntRange>()
        for (i in 0 until 10){
            if (listBitmap.size<=i){
                break
            }
            var loopCount=0
            val w=random.nextInt(bitmapDrawW/10)+bitmapDrawW/10//图标的宽，图标为正方形
            var x: Int
            var y: Int
            do {
                x=random.nextInt(bitmapDrawW-w)
                y=random.nextInt(bitmapDrawH-w)
                loopCount++
            }while ((xRange.any { x in it }||yRange.any{ y in it })&&loopCount<20)
            xRange.add(x until (x+w))
            yRange.add(y until (y+w))

            val b=listBitmap[i]
            val intrinsicW=b.width
            val intrinsicH=b.height
            val sx=w.toFloat()/intrinsicW
            val sy=w.toFloat()/intrinsicH
            matrix.also {
                it.reset()
                it.postScale(sx,sy,0f,0f)
                it.postTranslate(x.toFloat(),y.toFloat()+bitmapRect.top)
            }
            c.drawBitmap(b,matrix,paint)
        }
        bitmapPaint.shader=BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
    }
}