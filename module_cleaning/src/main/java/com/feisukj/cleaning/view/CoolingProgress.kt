package com.feisukj.cleaning.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.feisukj.cleaning.R

class CoolingProgress @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defSty:Int=0): View(context,attributeSet,defSty) {
    private lateinit var sweepGradient: SweepGradient
    private val outPaint= Paint().apply {
        this.isAntiAlias=true
        this.color=Color.WHITE
        this.strokeWidth=resources.displayMetrics.density*2
        this.style=Paint.Style.STROKE
    }
    private val innerPaint=Paint().apply {
        this.isAntiAlias=true
    }
    private val rect=RectF()
    private val arcMatrix=Matrix()
    var progress=0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val colors=IntArray(4){
            Color.parseColor("#00FFFFFF")
        }
        colors[0]=ContextCompat.getColor(context, R.color.cooling_progress_start)
        colors[1]=ContextCompat.getColor(context,R.color.cooling_progress_end)
        val positions=FloatArray(4)
        positions[0]=0f
        positions[1]=0.083f
        positions[2]=0.083f
        positions[3]=1f
        sweepGradient=SweepGradient(width/2f,height/2f,colors,positions)
        innerPaint.shader = sweepGradient

        val cx=w/2f
        val cy=h/2f
        val lineWidth=outPaint.strokeWidth/2
        rect.let {
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
        canvas.drawCircle(rect.centerX(),rect.centerY(),rect.width()/2,outPaint)

        arcMatrix.reset()
        arcMatrix.setRotate(progress*360,rect.centerX(),rect.centerY())
        canvas.setMatrix(arcMatrix)
        canvas.drawArc(rect,0f,360f,false,innerPaint)

    }
}