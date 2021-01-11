package com.example.module_tool.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.module_tool.R



/**
 * Author : Gupingping
 * Date : 2019/3/14
 * QQ : 464955343
 * 水平仪中的view
 *
 * 背景和气泡都自己绘制
 */
class HorizontalView @JvmOverloads constructor(context: Context, attrs:AttributeSet?=null, defStyleRes:Int=0) : View(context,attrs,defStyleRes) {
    private var backgroundSrc: Bitmap? = null//靶心源图
    private var leftTopSrc: Bitmap? = null//x、y源图
    private var bubbleSrc: Bitmap? = null//气泡源图
    private val bubbleMatrix = Matrix()//缩放
    private var bigBubble: Bitmap? = null//大气泡
    private var smallBubble: Bitmap? = null//小气泡

    private val bubbleMatrix2 = Matrix()//二缩放
    private var bigBubbleRadius: Int = 0//大气泡半径
    private var smallBubbleRadius: Int = 0//小气泡半径

    private var paint = Paint()//画背景的笔
    private var redPaint = Paint()//画重合提示红点的笔
    private var testPaint = Paint()

    private var mWidth: Int = 0//当前view的宽高

    //定义水平仪中气泡相对原点（0,0）的X、Y坐标
    var bubbleX: Int = 0
    var bubbleY: Int = 0

    //定义左侧和下边滑道中气泡相对原点（0,0）的X、Y坐标
    var smallBubbleX: Int = 0
    var smallBubbleY: Int = 0

    var limtX = 0F//x轴越界坐标
    var limtY = 0F//y轴越界坐标
    var horizontalAngle = 0f//x水平角度
    var verticalAngle = 0f//y水平角度

    var offsetLeft = 0F//左边气泡Y=0.0时，和滑道中心的偏移量
    val sideWidth = 40//滑道中间空白间距


    val sideRect = Rect()//侧边滑道原图
    val sideDst = Rect()//侧边滑道缩放后，宽等于气泡半径,长等于靶心半径-气泡半径

    val src = Rect()//靶心原图Rect
    val dst = Rect(0, 0, mWidth / 2, mWidth / 2)//靶心缩放后Rect

    init {
        init(context)
    }
    private fun init(context: Context) {
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER
        paint.color = Color.WHITE

        redPaint.isAntiAlias = true
        redPaint.style = Paint.Style.FILL
        redPaint.color = Color.RED

        testPaint.isAntiAlias = true
        testPaint.style = Paint.Style.STROKE
        testPaint.color = Color.RED
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //解析绘制的bitmap
        decodeResource()
        //绘制背景 靶心，左边，下边
        drawBackground(canvas)
        //处理两边气泡和重合提示
        twoSidebubble(canvas)
        //处理中间气泡移动，和边界问题
        centerBubble(canvas)
    }

    /**
     * 解析绘制的bitmap
     */
    private fun decodeResource() {
        if (backgroundSrc == null) {
            //解析背景资源
            ScaleBackground()
        }
        if (leftTopSrc == null) {
            //解析侧边资源
            ScaleLeft()
        }
        if (bubbleSrc == null) {
            //解析气泡资源
            ScaleBobble()
        }
    }

    var lastOffsetX = 0f//上一次没有出圆圈时x
    var lastOffsetY = 0f//上一次没有出圆圈时y
    /**
     * 处理中间气泡移动，和边界问题
     */
    private fun centerBubble(canvas: Canvas) {
        //----------------------
//        canvas.drawCircle(bubbleX.toFloat(), bubbleY.toFloat(), 5f, redPaint)
        if (horizontalAngle == 0.0f && verticalAngle == 0.0f) {
            canvas.drawCircle(mWidth / 2f, mWidth / 2f, bigBubbleRadius.toFloat()-3, redPaint)//绘制气泡中间水平时的提示红点
        }

        val offsetX = bubbleX.toFloat() - (mWidth / 2f - bigBubbleRadius)
        val offsetY = bubbleY.toFloat() - (mWidth / 2f - bigBubbleRadius)

        canvas.restore()
        canvas.save()
        if (Math.pow(offsetX.toDouble(), 2.0) + Math.pow(offsetY.toDouble(), 2.0) <= Math.pow((mWidth / 4 - bigBubbleRadius).toDouble(), 2.0)) {
            lastOffsetX = offsetX
            lastOffsetY = offsetY
            canvas.translate(offsetX, offsetY)
            canvas.drawBitmap(bigBubble!!, mWidth / 2f - bigBubbleRadius, mWidth / 2f - bigBubbleRadius, paint)
        } else {

            canvas.translate(lastOffsetX, lastOffsetY)
            canvas.drawBitmap(bigBubble!!, mWidth / 2f - bigBubbleRadius, mWidth / 2f - bigBubbleRadius, paint)


//            LogUtils.e("getOffset offsetX==${getOffset(bubbleX, bubbleY)[0]},offsetY==${getOffset(bubbleX, bubbleY)[1]}")
//            val offsetX = getOffset(bubbleX, bubbleY)[0].toFloat()
//            val offsetY = getOffset(bubbleX, bubbleY)[1].toFloat()
//
//            canvas.translate(offsetX, offsetY
//            )
//            canvas.drawBitmap(bigBubble!!, mWidth / 2f - bigBubbleRadius, mWidth / 2f - bigBubbleRadius, paint)
        }

    }

    private fun getOffset(bubbleX: Int, bubbleY: Int): Array<Double> {
        //勾股定理和相似三角形比例
        val offsetX = Math.sqrt(Math.pow(((mWidth / 4 - bigBubbleRadius).toDouble()), 2.0) / (1 + Math.pow(bubbleY.toDouble(), 2.0) / Math.pow(bubbleX.toDouble(), 2.0)))
        val offsetY = (bubbleY * offsetX) / bubbleX
//        LogUtils.e("getOffset offsetX==$offsetX,offsetY==$offsetY")
        return arrayOf(offsetX, offsetY)
    }


    /**
     * 绘制背景 靶心，左边，下边
     */
    private fun HorizontalView.drawBackground(canvas: Canvas) {
        canvas.save()//保存空白画布1
        canvas.save()//保存空白画布2
        canvas.save()//3
        canvas.save()//4
        canvas.save()//5
        canvas.save()//6
        //-------绘制左上滑道
        canvas.translate((mWidth / 8).toFloat() - sideWidth, (mWidth / 4).toFloat())
        canvas.drawBitmap(leftTopSrc!!, sideRect, sideDst, paint)

        //-------绘制左下滑道
        canvas.restore()//取出空白画布1
        canvas.translate((mWidth / 8).toFloat() + sideWidth, mWidth.toFloat() * 3 / 4)
        canvas.rotate(180f)
        canvas.drawBitmap(leftTopSrc!!, sideRect, sideDst, paint)

        canvas.restore()//取出空白画布2
        //-------绘制背景将靶心，画布平移到view四分之一的点开始绘制
        canvas.translate((mWidth / 4).toFloat(), (mWidth / 4).toFloat())
        canvas.drawBitmap(backgroundSrc!!, src, dst, paint)

        canvas.restore()//3
        //-------绘制下左滑道
        canvas.rotate(-90f, (mWidth / 2).toFloat(), (mWidth / 2).toFloat())
        canvas.translate((mWidth / 8).toFloat() - sideWidth, (mWidth / 4).toFloat())
        canvas.drawBitmap(leftTopSrc!!, sideRect, sideDst, paint)

        canvas.restore()//4
        //-------绘制下右滑道
        canvas.rotate(-90f, (mWidth / 2).toFloat(), (mWidth / 2).toFloat())
        canvas.translate((mWidth / 8).toFloat() + sideWidth, mWidth.toFloat() * 3 / 4)
        canvas.rotate(180f)
        canvas.drawBitmap(leftTopSrc!!, sideRect, sideDst, paint)
        canvas.restore()//5

    }

    /**
     * 处理两边气泡和重合提示
     */
    private fun HorizontalView.twoSidebubble(canvas: Canvas) {
        //限制左边气泡在滑道内
        if (smallBubbleY > (mWidth / 4).toFloat() && smallBubbleY < mWidth.toFloat() * 3 / 4 - smallBubbleRadius * 2) {
            if (verticalAngle == 0.0f) {
                offsetLeft = (mWidth / 2 - smallBubbleY).toFloat() - sideWidth
                canvas.drawCircle((mWidth / 8).toFloat(), (mWidth / 2).toFloat(), 39f, redPaint)//绘制气泡水平时的提示红点
            }
            canvas.drawBitmap(smallBubble!!, (mWidth / 8).toFloat() - smallBubbleRadius, smallBubbleY.toFloat() + offsetLeft, paint)//左气泡
        } else if (smallBubbleY <= mWidth / 4) {//最小值
            smallBubbleY = mWidth / 4
            canvas.drawBitmap(smallBubble!!, (mWidth / 8).toFloat() - smallBubbleRadius, smallBubbleY.toFloat(), paint)//左气泡
        } else if (smallBubbleY >= mWidth.toFloat() * 3 / 4 - smallBubbleRadius * 2) {//最大值
            smallBubbleY = mWidth * 3 / 4 - smallBubbleRadius * 2
            canvas.drawBitmap(smallBubble!!, (mWidth / 8).toFloat() - smallBubbleRadius, smallBubbleY.toFloat(), paint)//左气泡
        }


        //限制下边气泡在滑道内
        if (smallBubbleX > (mWidth / 4).toFloat() && smallBubbleX < mWidth.toFloat() * 3 / 4 - smallBubbleRadius * 2) {
            if (horizontalAngle == 0.0f) {
                offsetLeft = (mWidth / 2 - smallBubbleX).toFloat() - sideWidth
                canvas.drawCircle((mWidth / 2).toFloat(), (mWidth * 7 / 8).toFloat(), 39f, redPaint)//绘制气泡水平时的提示红点
            }
            canvas.drawBitmap(smallBubble!!, smallBubbleX.toFloat() + offsetLeft, (mWidth * 7 / 8).toFloat() - smallBubbleRadius, paint)//下气泡
        } else if (bubbleX <= (mWidth / 4).toFloat()) {
            smallBubbleX = mWidth / 4
            canvas.drawBitmap(smallBubble!!, smallBubbleX.toFloat(), (mWidth * 7 / 8).toFloat() - smallBubbleRadius, paint)//下气泡
        } else if (smallBubbleX >= mWidth.toFloat() * 3 / 4 - smallBubbleRadius * 2) {
            smallBubbleX = mWidth * 3 / 4 - smallBubbleRadius * 2
            canvas.drawBitmap(smallBubble!!, smallBubbleX.toFloat(), (mWidth * 7 / 8).toFloat() - smallBubbleRadius, paint)//下气泡
        }
    }

    private fun ScaleBobble() {
        bubbleSrc = BitmapFactory.decodeResource(resources, R.drawable.bubble)
        val bobbleSrcWidth = bubbleSrc!!.width
        val bobbleSrcHeight = bubbleSrc!!.height
        val bobbleSrcScaleWidth = 0.45f   // 获取缩放比例
        val bobbleSrcScaleHeight = 0.45f  //获取缩放比例

        //缩放
        bubbleMatrix.postScale(bobbleSrcScaleWidth, bobbleSrcScaleHeight, (mWidth / 2).toFloat(), (mWidth / 2).toFloat())//设置Matrix以(px,py)为轴心进行缩放，sx、sy为X、Y方向上的缩放比例
        bigBubble = Bitmap.createBitmap(bubbleSrc!!, 0, 0, bobbleSrcWidth, bobbleSrcHeight, bubbleMatrix, true)
        //缩放
        bubbleMatrix2.postScale(0.3f, 0.3f, (mWidth / 2).toFloat(), (mWidth / 2).toFloat())//设置Matrix以(px,py)为轴心进行缩放，sx、sy为X、Y方向上的缩放比例
        smallBubble = Bitmap.createBitmap(bubbleSrc!!, 0, 0, bobbleSrcWidth, bobbleSrcHeight, bubbleMatrix2, true)
        bigBubbleRadius = bigBubble!!.width / 2
        smallBubbleRadius = smallBubble!!.width / 2

    }

    private fun ScaleLeft() {
        leftTopSrc = BitmapFactory.decodeResource(resources, R.drawable.side).copy(Bitmap.Config.ARGB_8888, true)
        sideRect.left = 0
        sideRect.top = 0
        sideRect.right = leftTopSrc!!.width
        sideRect.bottom = leftTopSrc!!.height
        sideDst.left = 0
        sideDst.top = 0
        sideDst.right = sideWidth * 2
        sideDst.bottom = mWidth / 4 - sideWidth
//        LogUtils.e("tttt", "leftTopSrcWidth==$leftTopSrcWidth,singleBitmapWidth==$singleBitmapWidth")
    }

    private fun ScaleBackground() {
        backgroundSrc = BitmapFactory.decodeResource(resources, R.drawable.target).copy(Bitmap.Config.ARGB_8888, true)//一个只读的，无法对其进行处理,必须使用Bitmap.createBitmap()
        //获取源图大小的Rect,绘制到view二分之一长宽的Rect
        src.left = 0
        src.top = 0
        src.right = backgroundSrc!!.width
        src.bottom = backgroundSrc!!.height
        dst.left = 0
        dst.top = 0
        dst.right = mWidth / 2
        dst.bottom = mWidth / 2

    }

    fun updateView(values: FloatArray, xf: String, yf: String) {
        //获取与Y轴的夹角
        val yAngle = values[1]
        //获取与Z轴的夹角
        val zAngle = values[2]
        horizontalAngle = xf.toFloat()
        verticalAngle = yf.toFloat()
        //气泡位于中间时（水平仪完全水平）
        var x = mWidth / 2 - bigBubbleRadius
        var y = mWidth / 2 - bigBubbleRadius


        val deltaX = (((mWidth / 2 - bigBubbleRadius) * zAngle / MAX_ANGLE)).toInt()
        x += deltaX
//        when {
//            Math.abs(zAngle) <= MAX_ANGLE -> {//如果与Z轴的倾斜角还在最大角度之内
//                //根据与Z轴的倾斜角度计算X坐标轴的变化值
//                val deltaX = (((mWidth / 2 - bigBubbleRadius) * zAngle / MAX_ANGLE)).toInt()
//                x += deltaX
//            }
//            zAngle > MAX_ANGLE -> {
//                x = mWidth //如果与Z轴的倾斜角已经大于MAX_ANGLE，气泡应到最左边
//            }
//
//            else -> {
//                x = mWidth - bigBubbleRadius//如果与Z轴的倾斜角已经小于负的Max_ANGLE,气泡应到最右边
//            }
//        }
        val deltaY = (((mWidth / 2 - bigBubbleRadius) * yAngle / MAX_ANGLE)).toInt()
        y += deltaY

//        when {
//            Math.abs(yAngle) <= MAX_ANGLE -> { //如果与Y轴的倾斜角还在最大角度之内
//                //根据与Z轴的倾斜角度计算X坐标轴的变化值
//                val deltaY = (((mWidth / 2 - bigBubbleRadius) * yAngle / MAX_ANGLE)).toInt()
//                y += deltaY
//            }
//            yAngle > MAX_ANGLE -> y = mWidth - bigBubbleRadius//如果与Y轴的倾斜角已经大于MAX_ANGLE，气泡应到最下边
//
//            else -> y = 0//如果与Y轴的倾斜角已经小于负的Max_ANGLE,气泡应到最上边
//        }

        //如果计算出来的X，Y坐标还位于水平仪的仪表盘之内，则更新水平仪气泡坐标
        bubbleX = x
        bubbleY = y
        smallBubbleX = x
        smallBubbleY = y
//        LogUtils.e("tttt", "yAngle==$yAngle,zAngle==$zAngle,bubbleX==$bubbleX,bubbleY==$bubbleY")

        //通知组件更新
        postInvalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        //获取父控件给予的可用宽高
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        mWidth = Math.min(widthSize, heightSize)
        if (widthMode == View.MeasureSpec.UNSPECIFIED) {//如果父控件对子控件（也就是当前view）不限制宽尺寸
            mWidth = heightSize//那指定当前view的宽度为父控件给予的heightSize
        } else if (heightMode == View.MeasureSpec.UNSPECIFIED) {//如果父控件对子控件（也就是当前view）不限制高尺寸
            mWidth = widthSize//那指定当前view的宽度为父控件给予的widthSize
        }
        setMeasuredDimension(mWidth, mWidth)
    }


    companion object {
        const val MAX_ANGLE = 30
    }
}
