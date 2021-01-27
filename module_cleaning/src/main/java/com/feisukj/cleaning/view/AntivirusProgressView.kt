package com.feisukj.cleaning.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.feisukj.cleaning.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class AntivirusProgressView @JvmOverloads constructor(context: Context,attrs:AttributeSet?=null,defSty:Int=0): View(context,attrs,defSty) {
    private val progressBgPaint= Paint().apply {
        this.isAntiAlias=true
        this.style=Paint.Style.STROKE
        this.color= ContextCompat.getColor(context,R.color.antivirus_progress_bg)
        this.strokeWidth=resources.displayMetrics.density*3
    }
    private val progressPaint=Paint().apply {
        this.isAntiAlias=true
        this.style=Paint.Style.STROKE
        this.color=ContextCompat.getColor(context,R.color.antivirus_progress)
        this.strokeWidth=progressBgPaint.strokeWidth
        this.strokeCap=Paint.Cap.ROUND
    }
    private val progressRect=RectF()
    var progressEndBitmap:Bitmap?=null

    var progress=0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val cx=w/2f
        val cy=h/2f
        val mar=listOfNotNull(progressBgPaint.strokeWidth,progressEndBitmap?.width?.toFloat(), progressEndBitmap?.height?.toFloat()).max()?:0f
        val lineWidth=mar/2
        progressRect.let {
            val l=if (w<h) w else h
            it.left=cx-l/2+lineWidth
            it.top=cy-l/2+lineWidth
            it.right=cx+l/2-lineWidth
            it.bottom=cy+l/2-lineWidth
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        canvas.drawCircle(progressRect.centerX(),progressRect.centerY(),progressRect.width()/2,progressBgPaint)

        val angle=progress*360
        canvas.drawArc(progressRect,-90f,angle,false,progressPaint)
        progressEndBitmap?.let {
            val a=(-90+angle)/180*PI
            val x= cos(a).toFloat()*progressRect.width()/2-it.width/2
            val y= sin(a).toFloat()*progressRect.width()/2-it.height/2
//            x+progressRect.centerX()
//            y+progressRect.centerY()
            canvas.drawBitmap(it,x+progressRect.centerX(),y+progressRect.centerY(),progressBgPaint)
        }
    }
}