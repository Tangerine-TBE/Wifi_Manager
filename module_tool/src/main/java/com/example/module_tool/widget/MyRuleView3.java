package com.example.module_tool.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.example.module_base.base.BaseApplication;
import com.example.module_base.utils.SPUtil;
import com.example.module_tool.R;

import java.util.ArrayList;

/**
 * 横向滚动的刻度尺
 * Created by zhangqingbin on 2018/1/5.
 * https://github.com/CodingfarmerBin/ScrollDividingRule
 */

public class MyRuleView3 extends View {

    private Scroller mScroller;
    private Paint mPaint;
    private Paint mPaint2;
    private VelocityTracker mVelocityTracker;
    private OnScrollListener mListener;
    /**刻度间距*/
    private float mScaleMargin;
    /**总刻度宽度*/
    private float mScaleWidth;
    /**文字大小*/
    private float mTextSize;
    /**所有的刻度*/
    private ArrayList<String> mTotalTextList;
    /**线高度*/
    private float mLineHeight;
    /**总高度*/
    private float mRectHeight;
    private float mScrollLastX;
    /**滑动的总距离*/
    private float lastXValue;
    /**
     * 初始距离
     * */
    private int mInitDistance;
    /**文字距线的距离*/
    private int mTextLineMargin;//
    /**余数刻度*/
    private long mLastInstance;//
    /**开始金额*/
    private long mStartMoney;//
    /**每个小格的单位*/
    private int mUnit;
    /**结束金额*/
    private long mFinalMoney;//

    private Bitmap sliderBitmap;
    private Bitmap guideBitmap1;
    private Bitmap guideBitmap2;
    private Bitmap guideBitmap3;

    private String GUIDE_KEY = "guide_key";

    public MyRuleView3(Context context) {
        this(context, null);
    }

    public MyRuleView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRuleView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                String name = attrs.getAttributeName(i);
                if ("layout_width".equals(name)) {
                    String value = attrs.getAttributeValue(i);
                    if (value.length() > 2) {
                        if (value.endsWith("dp")) {
                            float margin = Float.valueOf(value.substring(0, value.length() - 2));
                            mScaleWidth = Utils.dp2px(context, margin);
                        } else {
                            mScaleWidth = Float.valueOf(value.substring(0, value.length() - 2));
                        }
                    } else if (value.equals("-1") || value.equals("-2") || value.equals("0")) {
                        mScaleWidth = 0;
                    }
                } else if ("line_height".equals(name)) {
                    String value = attrs.getAttributeValue(i);
                    if (value.length() > 2) {
                        if (value.endsWith("dp")) {
                            mLineHeight =
                                    Utils.dp2px(context, Float.valueOf(value.substring(0, value.length() - 2)));
                        } else {
                            mLineHeight = Float.valueOf(value.substring(0, value.length() - 2));
                        }
                    } else {
                        mLineHeight = 50;
                    }
                } else if ("dividing_text_size".equals(name)) {
                    String value = attrs.getAttributeValue(i);
                    if (value.length() > 2) {
                        if (value.endsWith("sp")) {
                            mTextSize =
                                    Utils.sp2px(context, Float.valueOf(value.substring(0, value.length() - 2)));
                        } else {
                            mTextSize = Float.valueOf(value.substring(0, value.length() - 2));
                        }
                    } else {
                        mTextSize = 32;
                    }
                }
            }
            sliderBitmap = getSliderBitmap();
            guideBitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_ruler_guide_1);
            guideBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_ruler_guide_2);
            guideBitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_ruler_guide_3);

            //如果之前教程未完成，继续教程
            if (SPUtil.getInstance().getInt(GUIDE_KEY,0) == 3)
                SPUtil.getInstance().putInt(GUIDE_KEY,1);
        }
        // 画笔
        mPaint = new Paint();
        mPaint2 = new Paint();

        //总的高度，因为text的高度和设置的textSize会有误差所以加上20的高度
        mRectHeight = mLineHeight + mTextSize + mTextLineMargin + 20;
        //初始设置每个刻度间距为30px
        mScaleMargin = 20;
        mTotalTextList = new ArrayList<>();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255,255,255,255);
        mPaint.setColor(Color.GRAY);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mPaint.setDither(true);
        // 空心
        mPaint.setStyle(Paint.Style.STROKE);
        // 文字居中
        mPaint.setTextAlign(Paint.Align.CENTER);
        onDrawScale(canvas, mPaint); //画刻度
        onDrawLine(canvas, mPaint);
        onDrawGuide(canvas, mPaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec((int) mRectHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mInitDistance = Utils.dp2px(getContext(),16f);
        lastXValue = 10 * mScaleMargin + mInitDistance;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int[] mColors = {Color.parseColor("#3effffff"),Color.parseColor("#3eFEB68D")};
        LinearGradient linearGradient = new LinearGradient(0, 0, 0, getHeight(), mColors, null, Shader.TileMode.MIRROR);
        mPaint2.setShader(linearGradient);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        float x =  event.getX();
        float y =  event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScrollLastX = x;
                if (SPUtil.getInstance().getInt(GUIDE_KEY,0)==3&&inGuide2(x,y)){
                    BaseApplication.mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SPUtil.getInstance().putInt(GUIDE_KEY,4);
                            postInvalidate();
                        }
                    },300L);
                    toNowScale();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (SPUtil.getInstance().getInt(GUIDE_KEY,0)!=3){
                    float dataX = mScrollLastX - x;
                    mScrollLastX = x;
                    lastXValue -= dataX;
                    dealActionUp();
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return true;
        }
        return super.onTouchEvent(event);
    }

    //是否是点击guide2
    private boolean inGuide2(float x,float y){
        float guide2Left = 0;
        if (lastXValue - mScroller.getFinalX()>getWidth()/2f)
            guide2Left = lastXValue - guideBitmap2.getWidth()-sliderBitmap.getWidth();
        else
            guide2Left = lastXValue + sliderBitmap.getWidth();
        return x >= guide2Left&& x<= guide2Left+guideBitmap2.getWidth() && y >= (getHeight()-guideBitmap2.getHeight())/2f&& y <= (getHeight()+guideBitmap2.getHeight())/2f;
    }

    /**
     * 处理手势抬起之后的操作
     *
     */
    private void dealActionUp() {
        if (lastXValue<mInitDistance)
            lastXValue = mInitDistance;
        else if(lastXValue>(mTotalTextList.size()-1)*mScaleMargin+mInitDistance){
            lastXValue = (mTotalTextList.size()-1)*mScaleMargin+mInitDistance;
        }
        if (mListener != null) {
            mListener.onScaleScrollChanged(
                    (long) ((lastXValue-mInitDistance) / mScaleMargin  + mStartMoney));//返回滚动选中的位置
        }
        postInvalidate();
    }

    private void onDrawScale(Canvas canvas, Paint paint) {
        paint.setAntiAlias(true);
        paint.setTextSize(mTextSize);
        paint.setStyle(Paint.Style.FILL);
        int height = getHeight();
        for (int i = 0, k = 0; i < mTotalTextList.size(); i++) {
                if (i % 10 == 0) { //整值
                    paint.setColor(Color.GRAY);
                    canvas.drawLine(i * mScaleMargin + mInitDistance, height,
                            i * mScaleMargin + mInitDistance, height - mLineHeight,
                            paint);
                    canvas.drawLine(i * mScaleMargin + mInitDistance,0,i * mScaleMargin + mInitDistance,mLineHeight,paint);
                    //整值文字
                    paint.setColor(Color.GRAY);
                    canvas.drawText(mTotalTextList.get(k), i * mScaleMargin + mInitDistance,
                            height - mLineHeight - mTextLineMargin, paint);
                    canvas.drawText(mTotalTextList.get(k), i * mScaleMargin + mInitDistance,
                            mLineHeight + mTextLineMargin, paint);
                    k++;
                } else if(i % 5 == 0){
                    paint.setColor(Color.GRAY);
                    canvas.drawLine(i * mScaleMargin + mInitDistance, height,
                            i * mScaleMargin + mInitDistance, height - mLineHeight*0.75f,
                            paint);
                    canvas.drawLine(i * mScaleMargin + mInitDistance, 0,
                            i * mScaleMargin + mInitDistance, mLineHeight*0.75f,
                            paint);
                } else {
                    paint.setColor(Color.GRAY);
                    canvas.drawLine(i * mScaleMargin + mInitDistance, height,
                            i * mScaleMargin + mInitDistance, height - mLineHeight*0.5f,
                            paint);
                    canvas.drawLine(i * mScaleMargin + mInitDistance, 0,
                            i * mScaleMargin + mInitDistance, mLineHeight*0.5f,
                            paint);
                }
        }
    }

    private void onDrawLine(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(2);
        paint.setColor(Color.parseColor("#FF9436"));
        canvas.drawLine(lastXValue, 0, lastXValue, getHeight(), paint);
//        canvas.drawRect(mInitDistance, 0, lastXValue + mInitDistance, getHeight(), mPaint2);
        canvas.drawRect(mInitDistance, 0, lastXValue, getHeight(), mPaint2);
        paint.setColor(Color.GRAY);
        canvas.drawBitmap(sliderBitmap,lastXValue-sliderBitmap.getWidth()/2f,(getHeight()-sliderBitmap.getHeight())/2f,paint);
    }

    private void onDrawGuide(Canvas canvas, Paint paint){
        if (SPUtil.getInstance().getInt(GUIDE_KEY,0) == 0){
            canvas.drawBitmap(guideBitmap1,lastXValue-guideBitmap1.getWidth()/2f,(getHeight()-sliderBitmap.getHeight())/2f-guideBitmap1.getHeight(),paint);
            SPUtil.getInstance().putInt(GUIDE_KEY,1);
        }else if (SPUtil.getInstance().getInt(GUIDE_KEY,0) == 2){
            float guide2Left = 0;
            if (lastXValue - mScroller.getFinalX()>getWidth()/2f)
                guide2Left = lastXValue - guideBitmap2.getWidth()-sliderBitmap.getWidth();
            else
                guide2Left = lastXValue + sliderBitmap.getWidth();
            canvas.drawBitmap(guideBitmap2,guide2Left,(getHeight()-guideBitmap2.getHeight())/2f,paint);
            SPUtil.getInstance().putInt(GUIDE_KEY,3);
        }else if (SPUtil.getInstance().getInt(GUIDE_KEY,0) == 4){
            canvas.drawBitmap(guideBitmap3,lastXValue + sliderBitmap.getWidth(),(getHeight()-guideBitmap3.getHeight())/2f,paint);
            SPUtil.getInstance().putInt(GUIDE_KEY,5);
        }
    }

    /**
     * 使用Scroller的时候需要重写该方法
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    private void smoothScrollBy(float dx, float dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), (int) dx, (int) dy);
        postInvalidate();
    }

    public interface OnScrollListener {
        void onScaleScrollChanged(long scale);
    }

    /**
     * 设置当前位置
     *
     */
    public void toNowScale() {
        float i = lastXValue-mInitDistance;
        smoothScrollBy((i-mScroller.getFinalX()),0);
    }

    public void toReSet() {
        lastXValue = 10 * mScaleMargin + mInitDistance;
        smoothScrollBy(-mScroller.getFinalX(),0);
        dealActionUp();
        postInvalidate();
    }

    public boolean canContinue(){
        if (SPUtil.getInstance().getInt(GUIDE_KEY) == 1){
            SPUtil.getInstance().putInt(GUIDE_KEY,2);
            postInvalidate();
        }
        return SPUtil.getInstance().getInt(GUIDE_KEY) > 3;
    }

    /**
     * 初始化数据
     *
     * @param startMoney 开始金额
     * @param finalMoney 最终金额
     * @param unit 每个刻度单位
     * @param listener 滚动监听
     */
    public void bindMoneyData(int startMoney, int finalMoney, int unit, OnScrollListener listener) {
        if (mTotalTextList != null && mTotalTextList.size() > 0) {
            refresh(startMoney, finalMoney);
        } else {
            mUnit = unit;
            mStartMoney = startMoney / unit;
            mTotalTextList = new ArrayList<>();
            mFinalMoney = finalMoney;
            for (int i = 0; i < (finalMoney - startMoney) / unit  + 1; i++) {
                mTotalTextList.add(String.valueOf(i * unit  + startMoney));//总共刻度数
            }
            mLastInstance = (long) (((finalMoney - startMoney) % unit ) * mScaleMargin);//余数刻度
            mScaleWidth = (mTotalTextList.size()-1) * mScaleMargin + mLastInstance;
            mListener = listener;
            postInvalidate();
        }
    }

    /**
     * 刷新操作
     */
    public void refresh(final int startMoney, final int finalMoney) {
        new Thread(new Runnable() {//防止数据量过多导致阻塞主线程
            @Override
            public void run() {
                mStartMoney = startMoney / mUnit;
                mFinalMoney = finalMoney;
                mTotalTextList.clear();
                for (int i = 0; i < (finalMoney - startMoney) / mUnit + 1; i++) {
                    mTotalTextList.add(String.valueOf(i * mUnit + startMoney));//总共刻度数
                }
                mLastInstance = (long) (((finalMoney - startMoney) % mUnit)  * mScaleMargin);//余数刻度
                mScaleWidth = (mTotalTextList.size() -1) * mScaleMargin + mLastInstance;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        postInvalidate();
                    }
                });
            }
        });
    }

    /**
     * 设置每个刻度间距
     *
     * @param margin 间距
     * @return 返回当前view 链式编程
     */
    public MyRuleView3 setScaleMargin(float margin) {
        mScaleMargin = margin;
        mRectHeight = (int) (mLineHeight + mTextSize + mTextLineMargin + 20);
        return this;
    }

    /**
     * 设置文字和刻度线的间距
     */
    public MyRuleView3 setTextLineMargin(int textLineMargin) {
        mTextLineMargin = textLineMargin;
        mRectHeight = (int) (mLineHeight + mTextSize + mTextLineMargin + 20);
        return this;
    }

    public static class Utils {
        /**
         * dp转px
         *
         * @param context
         * @param dpVal
         * @return
         */
        public static int dp2px(Context context, float dpVal) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dpVal, context.getResources().getDisplayMetrics());
        }

        /**
         * sp转px
         */
        public static int sp2px(Context context, float spVal) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,

                    spVal, context.getResources().getDisplayMetrics());
        }

        /**
         * px转dp
         */
        public static float px2dp(Context context, float pxVal) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (pxVal / scale);
        }



        /**
         * px转sp
         */
        public static float px2sp(Context context, float pxVal) {
            return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
        }


    }

    private Bitmap getSliderBitmap(){
        Bitmap bitmap=null;
        Context context = getContext();
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(R.drawable.ic_slider);
            bitmap = Bitmap.createBitmap(Utils.dp2px(context,46.31f), Utils.dp2px(context,31f) , Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_slider);
        }
        return bitmap;
    }
}
