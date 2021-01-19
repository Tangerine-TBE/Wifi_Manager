package com.example.wifi_manager.ui.widget

import android.content.Context
import android.graphics.*
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
    companion object{
        const val WIFI_CONNECT="建立连接"
        const val WIFI_WRITE_PWD="写入密码"
        const val WIFI_CHECK_PWD="验证密码"
        const val WIFI_DISPENSE_IP="分配IP地址"
        const val WIFI_CHECK_LOGIN="可能需要登录验证"
        const val WIFI_OPEN="开放WiFi"
    }
    private val mBgCirclePaint= Paint()
    private val mTextPaint= Paint()
    private var mWidth=0f
    private var mHeight=0f
    private val minRadius=SizeUtils.dip2px(context,2f).toFloat()
    private val middleRadius=SizeUtils.dip2px(context,5f).toFloat()
    private val bigOneRadius=SizeUtils.dip2px(context,6f).toFloat()
    private val bigTwoRadius=SizeUtils.dip2px(context,7f).toFloat()
    private val textMarginTop=SizeUtils.dip2px(context,30f).toFloat()
    private val titleSize=SizeUtils.sp2px(context,12f).toFloat()
    private val stepingColor=ContextCompat.getColor(context,R.color.theme_color)
    private val bgColor=ContextCompat.getColor(context,R.color.bg_circle_color)
    private val outOneColor=ContextCompat.getColor(context,R.color.big_out_one_color)
    private val outTwoColor=ContextCompat.getColor(context,R.color.big_out_two_color)
    private val mFinishIcon:Bitmap
    private var currentState= StepState.NONE
    private var mOpen=true
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

        mFinishIcon= BitmapFactory.decodeResource(resources, R.mipmap.icon_connect_finish)


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
    private var finishIconRadius=1f




    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bigMargin = mWidth*0.125f
        bigStep= mWidth*0.25f
        minStep=mWidth*0.04f
        minMargin=mWidth*0.19f
        mFinishIcon?.apply {
            finishIconRadius= width.toFloat()/2
        }

        drawQuietState(canvas)
        drawActiveState(canvas)

    }

    private fun drawActiveState(canvas: Canvas) {
        canvas.withTranslation(0f,mHeight/2) {
            LogUtils.i("-----drawActiveState---$currentState----------------------")
            when (currentState) {

                StepState.ONE -> {
                    drawActiveLoading(0)
                    drawActiveText(0)
                }
                StepState.TWO -> {
                    drawActiveLoading(1)
                    drawActiveText(1)
                    drawActiveSuccess(0)
                    mBgCirclePaint.color=bgColor
                    drawStep(1,stepingColor)
                }
                StepState.THREE -> {
                    drawActiveLoading(2)
                    drawActiveText(2)
                    drawActiveSuccess(1)
                    drawStep(2,stepingColor)
                }
                StepState.FOUR -> {
                    drawActiveLoading(3)
                    drawActiveText(3)
                    drawActiveSuccess(2)
                    drawStep(3,stepingColor)
                }
                StepState.FIVE-> {
                    drawActiveText(3)
                    drawActiveSuccess(3)
                    drawStep(3,stepingColor)
                }
                StepState.NONE->{

                }

            }

        }
    }

    private fun Canvas.drawActiveLoading(step: Int) {
        mBgCirclePaint.color = outTwoColor
        drawCircle(bigMargin + (bigStep * step), 0f, bigTwoRadius, mBgCirclePaint)

        mBgCirclePaint.color = outOneColor
        drawCircle(bigMargin + (bigStep * step), 0f, bigOneRadius, mBgCirclePaint)

        mBgCirclePaint.color = stepingColor
        drawCircle(bigMargin + (bigStep * step), 0f, middleRadius, mBgCirclePaint)
    }

    private fun Canvas.drawActiveText(step:Int) {
        mTextPaint.color = stepingColor
        drawText(textPosition(step, mOpen), bigMargin + (bigStep * step), textMarginTop, mTextPaint)
    }

    //画成功的圆
    private fun Canvas.drawActiveSuccess(step: Int) {
        drawFilter = PaintFlagsDrawFilter(0,  Paint.FILTER_BITMAP_FLAG)
        for (i in 0 until step+1){
            drawBitmap(mFinishIcon,bigMargin+(bigStep*i)-finishIconRadius,-finishIconRadius,mBgCirclePaint)
        }
    }


    //画步骤点
    private fun Canvas.drawStep(step:Int,color: Int) {
        mBgCirclePaint.color=color
            for (i in 0 until step){
                for (j in 0 until 4){
                    drawCircle(minMargin+bigStep*i+(minStep*j),0f,minRadius,mBgCirclePaint)
                }
            }
    }


    private fun drawQuietState(canvas: Canvas) {
        canvas.withTranslation(0f,mHeight/2) {
            mBgCirclePaint.color=bgColor
            drawText()
            drawBgCircle()
            drawStep(3,bgColor)
        }


    }
    //画字
    private fun Canvas.drawText() {
            for (i in 0 until 4){
                mTextPaint.color=bgColor
                drawText(textPosition(i,mOpen),bigMargin+(bigStep*i),textMarginTop,mTextPaint)
            }
    }
    //画背景的圆
    private fun Canvas.drawBgCircle() {
            for (i in 0 until 4){
                drawCircle(bigMargin+(bigStep*i),0f,middleRadius,mBgCirclePaint)
            }
    }

    private  fun textPosition(position:Int,open:Boolean)=when(position){
        0->WIFI_CONNECT
        1-> if(open) WIFI_OPEN else WIFI_WRITE_PWD
        2->if (open)  WIFI_CHECK_LOGIN else WIFI_CHECK_PWD
        3->WIFI_DISPENSE_IP
        else->""
    }





    fun setStepState(state:StepState,open:Boolean=false){
        currentState=state
        mOpen=open
        invalidate()
    }



    enum class StepState{
        ONE,TWO,THREE,FOUR,FIVE,NONE
    }




}