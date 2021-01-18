package com.example.wifi_manager.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.withTranslation
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.SizeUtils
import com.example.wifi_manager.R
import com.example.wifi_manager.base.BaseView

/**
 * @author: 铭少
 * @date: 2021/1/18 0018
 * @description：
 */
class ConnectWifiView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseView(context, attrs, defStyleAttr) {
    private val mBgCirclePaint= Paint()
    private val mTextPaint= Paint()
    private var mWidth=0f
    private var mHeight=0f
    private val minRadius=SizeUtils.dip2px(context,2f).toFloat()
    private val middleRadius=SizeUtils.dip2px(context,5f).toFloat()
    private val bigRadius=SizeUtils.dip2px(context,9f).toFloat()
    private val textMarginTop=SizeUtils.dip2px(context,30f).toFloat()
    private val titleSize=SizeUtils.sp2px(context,12f).toFloat()
    private val stepingColor=ContextCompat.getColor(context,R.color.theme_color)
    private val bgColor=ContextCompat.getColor(context,R.color.bg_circle_color)

    init {
        mBgCirclePaint.apply {
            color=bgColor
            style=Paint.Style.FILL
            strokeWidth=1f
            isAntiAlias=true
        }
        mTextPaint.apply {
            color=bgColor
            style=Paint.Style.FILL
            textAlign=Paint.Align.CENTER
            strokeWidth=1f
            textSize=titleSize
            isAntiAlias=true
        }



    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = widthSize.toFloat()
        mHeight=heightSize.toFloat()
        setMeasuredDimension(widthSize,heightSize)
    }


    private var bigMargin=0f
    private var minMargin=0f
    private var bigStep=0f
    private var minStep=0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bigMargin = mWidth*0.125f
        bigStep= mWidth*0.25f
        minStep=mWidth*0.04f
        minMargin=mWidth*0.19f


        drawBgCircle(canvas)
        drawText(canvas)
        drawOneStep(canvas)
        drawTwoStep(canvas)
        drawThreeStep(canvas)


    }
    private fun drawThreeStep(canvas: Canvas) {
        canvas.withTranslation(0f,mHeight/2) {
            for (i in 0 until 4){
                mBgCirclePaint.color=Color.BLACK
                drawCircle(minMargin+bigStep*2+(minStep*i),0f,minRadius,mBgCirclePaint)
            }
        }
    }
    private fun drawTwoStep(canvas: Canvas) {
        canvas.withTranslation(0f,mHeight/2) {
            for (i in 0 until 4){
                mBgCirclePaint.color=Color.BLACK
                drawCircle(minMargin+bigStep+(minStep*i),0f,minRadius,mBgCirclePaint)
            }
        }
    }
    private fun drawOneStep(canvas: Canvas) {
        canvas.withTranslation(0f,mHeight/2) {
            for (i in 0 until 4){
                mBgCirclePaint.color=Color.BLACK
                drawCircle(minMargin+(minStep*i),0f,minRadius,mBgCirclePaint)
            }
        }
    }

    private fun drawText(canvas: Canvas) {
        canvas.withTranslation(0f,mHeight/2) {
            LogUtils.i("--drawText----$textMarginTop-----------------------")
            for (i in 0 until 4){
                mTextPaint.color=bgColor
                drawText(textPosition(i),bigMargin+(bigStep*i),textMarginTop,mTextPaint)
            }

            mTextPaint.color=stepingColor
            drawText(textPosition(currentStep),bigMargin+(bigStep*currentStep),textMarginTop,mTextPaint)
        }


    }

    private  fun textPosition(position:Int)=when(position){
        0->"建立连接"
        1->"写入密码"
        2->"验证密码"
        3->"分配IP地址"
        else->""
    }



    private fun drawBgCircle(canvas: Canvas) {
        canvas.withTranslation(0f,mHeight/2) {
            for (i in 0 until 4){
                drawCircle(bigMargin+(bigStep*i),0f,middleRadius,mBgCirclePaint)
            }

        }

    }



    private var currentStep=0
    fun setStepState(state:Int){
        currentStep=state
        invalidate()
    }

    enum class StepState{
        ONE,TWO,THREE,FOUR
    }



}