package com.example.module_tool.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.module_tool.R

import java.text.DecimalFormat


/**
 * Author : Gupingping
 * Date : 2019/3/14
 * QQ : 464955343
 * 测量距离activity内的直角view
 */
class RectView : View {
    private var paint = Paint()//红笔
    private var dashPaint = Paint()//虚线笔
    var zAngle = 0f//获取与Y轴的夹角
    var yAngle = 0f//获取与Z轴的夹角
    val pointX = ArrayList<Int>()
    val dashWidt = 30 //虚线宽度和间距
    var direction = 0//偏移方向

    private var zString: String = ""//旋转角度偏移
    private var yString: String = ""//旋转角度偏移

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()

    }

    private fun init() {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = Color.GREEN
        paint.strokeWidth = 5f
        paint.textSize = 40f

        dashPaint.isAntiAlias = true
        dashPaint.style = Paint.Style.FILL
        dashPaint.color = Color.GREEN
        dashPaint.strokeWidth = 5f

        for (i in 0..50 step 2) {
            pointX.add(i)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        //绘制虚线
        for (i in pointX) {
            canvas.drawLine((i * dashWidt).toFloat(), (width.toFloat() / 3).toFloat(), (i * dashWidt + dashWidt).toFloat(), (width.toFloat() / 3).toFloat(), dashPaint)
        }
        val format=context.getString(R.string.tip)
        val left=context.getString(R.string.left)
        val right=context.getString(R.string.right)
        val s=String.format(format,if (direction == 1) left else right,"$zString°")
        canvas.drawText(s, (width.toFloat() * 3 / 4) - 150, (width.toFloat() / 3) - 80, paint)
        canvas.restore()

        //旋转十字架
        canvas.rotate(zAngle, (width.toFloat() * 3 / 4) - 50, (width.toFloat() / 3))

        canvas.drawLine(0f, (width.toFloat() / 3), width.toFloat(), (width.toFloat() / 3), paint)

        canvas.drawLine((width.toFloat() * 3 / 4) - 50, 0f, (width.toFloat() * 3 / 4) - 50, height.toFloat(), paint)

        canvas.drawCircle((width.toFloat() * 3 / 4) - 50, (width.toFloat() / 3), 10f, paint)

    }


    fun updateView(acc: FloatArray, values: FloatArray) {
        //获取与Y轴的夹角
        yAngle = values[1]
        //获取与Z轴的夹角
        zAngle = values[2]

        direction = if (acc[2] > 0) 1 else -1

        val decimalFormat = DecimalFormat()
        decimalFormat.applyPattern("0.0")
        zString = decimalFormat.format(Math.abs(zAngle))
        yString = decimalFormat.format(Math.abs(yAngle))

        //通知组件更新
        postInvalidate()
    }
}
