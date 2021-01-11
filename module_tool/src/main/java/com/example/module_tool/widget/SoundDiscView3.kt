package com.example.module_tool.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.example.module_tool.R
import java.util.*


/**
 * Author : Gupingping
 * Date : 2019/3/14
 * QQ : 464955343
 * 背景和指针都自己绘制
 */
class SoundDiscView3 @JvmOverloads constructor(context: Context, attrs: AttributeSet): View(context,attrs) {
    private var background: Bitmap? = null//源图
    private var scaleBackground: Bitmap? = null//第一次缩放
    private var bitmap: Bitmap? = null//放大到和view宽高
    private val mMatrix = Matrix()//缩放
    private val matrixBig = Matrix()//放大

    private var needle: Bitmap? = null//源图
    private var scaleneedle: Bitmap? = null//第一次缩放
    private var needleBitmap: Bitmap? = null//放大到和view宽高
    private val needleMatrix = Matrix()//缩放
    private val needlematrixBig = Matrix()//放大

    private var paint: Paint? = null

    private var degree = 0f

    fun setDegree(degree: Int) {
        this.degree = (degree * 1.8 - 90).toFloat()//分贝值转换成角度值：分贝值满刻度算100，角度180，每增加1分贝增加1.8的角度，绘制的时候默认是在中间，所以要减去90°
        invalidate()
    }
    init {
        init()
    }
    private fun init() {
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.textAlign = Paint.Align.CENTER
        paint!!.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmap == null) {
            //背景设置
            ScaleBackground()
            //--------------指针----------------
            ScaleNeedle()
        }
        canvas.drawBitmap(scaleBackground!!, matrixBig, paint)//绘制背景
        canvas.save()
        canvas.rotate(degree, (width / 2).toFloat(), (height / 6 + scaleneedle!!.height - 20).toFloat()+50)
        canvas.drawBitmap(scaleneedle!!, needlematrixBig, paint)
        canvas.restore()
    }

    private fun ScaleNeedle() {
        needle = BitmapFactory.decodeResource(resources, R.drawable.noise_index)
        val needleWidth = needle!!.width
        val needleHeight = needle!!.height
        val needleScaleWidth = 0.35f  // 获取缩放比例
        val needleScaleHeight = 0.30f  //获取缩放比例
        //缩放
        needleMatrix.postScale(needleScaleWidth, needleScaleHeight)//设置Matrix以(px,py)为轴心进行缩放，sx、sy为X、Y方向上的缩放比例
        //重新创建缩放后的bitmap
        scaleneedle = Bitmap.createBitmap(needle!!, 0, 0, needleWidth, needleHeight, needleMatrix, true)
        //重新创建移动到中心后的bitmap
        needleBitmap = Bitmap.createBitmap(width / 2 - scaleneedle!!.width / 2, width / 6, scaleneedle!!.config)
        //移动
        needlematrixBig.setTranslate((width / 2 - scaleneedle!!.width / 2).toFloat(), (height / 6).toFloat()+50)
    }

    private fun ScaleBackground() {
        val locale: Locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //android 7.0及以上
            locale = resources.configuration.locales.get(0)
        } else {
            //android 7.0 以下
            locale = resources.configuration.locale
        }
        val lang:String=locale.language
        if (lang.length>=2){
            if (lang.contains("zh"))
                background = BitmapFactory.decodeResource(resources, R.drawable.noise_disc)//一个只读的，无法对其进行处理,必须使用Bitmap.createBitmap()
            else
                background = BitmapFactory.decodeResource(resources, R.drawable.noise_disc_e)//一个只读的，无法对其进行处理,必须使用Bitmap.createBitmap()
        }else{
            background = BitmapFactory.decodeResource(resources, R.drawable.noise_disc)//一个只读的，无法对其进行处理,必须使用Bitmap.createBitmap()
        }
        val bitmapWidth = background!!.width
        val bitmapHeight = background!!.height
        val scaleWidth = width.toFloat() / bitmapWidth.toFloat()  // 获取缩放比例
        val scaleHeight = height.toFloat() / bitmapHeight.toFloat()  //获取缩放比例
        //缩放
        mMatrix.postScale(scaleWidth, scaleHeight, (width / 2).toFloat(), (height / 2).toFloat())//设置Matrix以(px,py)为轴心进行缩放，sx、sy为X、Y方向上的缩放比例
        scaleBackground = Bitmap.createBitmap(background!!, 0, 0, bitmapWidth, bitmapHeight, mMatrix, true)
        bitmap = Bitmap.createBitmap(width, height, scaleBackground!!.config)
        val sw = (width / scaleBackground!!.width).toFloat()
        val sh = (height / scaleBackground!!.height).toFloat()
        //放大
        matrixBig.setScale(sw, sh, (width / 2).toFloat(), (height / 2).toFloat())//设置Matrix以(px,py)为轴心进行缩放，sx、sy为X、Y方向上的缩放比例
    }
}
