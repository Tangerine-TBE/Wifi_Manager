package com.example.module_tool.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.module_tool.utils.DeviceUtils;


/**
 * 顶层绘制矩形、用户操作界面
 * Created by zhangshixin on 4/2/2016.
 * Email : sxzhang2016@163.com
 */
public class RectControlView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = "RectControlView";

    private SurfaceHolder mHolder;
    private Context mContext;
    private static final int centerSize = 20;
    private static final int rulerPaddingX = 0;   //尺子距离X轴边距
    private static final float rulerLineWidth = 5;
    private float mScreenWidth;
    private float mScreenHeight;
    private float mHalfScreenWidth;   //屏幕宽度的一半
    private float mHalfScreenHeight;
    private Paint paint = new Paint();//中间的十字形
    private Paint rulerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//两条线的画笔
    private Float lastY1=0F;//两条线中一条线的最后y轴坐标
    private Float lastY2=0F;//两条线中一条线的最后y轴坐标
    private boolean canY1=true;
    private boolean canY2=true;
    private Paint spotPaint=new Paint();//画旁边的两点
    private Float spotRadius=60F;//点的半径
    private OnRulerHeightChangedListener mListener;

    float startX;
    float startY;

    public RectControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initRulerChangedListener(mContext);
        mHolder = getHolder();
        mHolder.addCallback(this);
        //关键的两步 : 设置透明、放到顶部
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        this.setZOrderOnTop(true);

        paint.setFlags(Paint.ANTI_ALIAS_FLAG);  //抗锯齿
        paint.setColor(Color.RED);
        paint.setStrokeWidth(rulerLineWidth);

        rulerPaint.setColor(Color.GREEN);
        rulerPaint.setStrokeWidth(rulerLineWidth);

        spotPaint.setColor(Color.RED);
        spotPaint.setStyle(Paint.Style.FILL);
    }

    private void initRulerChangedListener(Context context) {
        try {
            mListener = (OnRulerHeightChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString()
                    + "must implement OnRulerHeightChangedListener !");
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mScreenWidth = DeviceUtils.getScreenWidth(mContext);
        mScreenHeight = DeviceUtils.getScreenHeight(mContext);
        mHalfScreenWidth = mScreenWidth / 2;
        mHalfScreenHeight = mScreenHeight / 2;
        lastY1=mHalfScreenHeight;
        lastY2=mHalfScreenHeight;
        Log.e(TAG, "center : (" + mHalfScreenWidth + " , " + mHalfScreenHeight + ")");
        drawRulerLine(lastY1,lastY2);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    /**
     * 绘制尺子的那两条线
     *
     */
    private void drawRulerLine(Float yDistance1, Float yDistance2) {
        Canvas canvas = mHolder.lockCanvas();
//        canvas.drawColor(Color.BLACK);

        if (canvas == null) {
            Log.e(TAG, "failed to get canvas");
            return;
        }
        //清屏
        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(p);

        drawRulerLineOnCanvas(canvas, yDistance1,yDistance2);

        mHolder.unlockCanvasAndPost(canvas);

        //绘制到截图用的bitmap上
        Bitmap screenBitmap = Bitmap.createBitmap((int) mScreenWidth, (int) mScreenHeight, Bitmap.Config.RGB_565);
        Canvas screenCaptureCanvas = new Canvas(screenBitmap);
        drawRulerLineOnCanvas(screenCaptureCanvas, yDistance1,yDistance2);

        mListener.onRulerHeightChanged(Math.abs(yDistance1-yDistance2), screenBitmap);
    }

    /**
     * 在指定的Canvas绘制
     * @param canvas
     * @param yDistance1
     * @param yDistance2
     */
    private void drawRulerLineOnCanvas(Canvas canvas, Float yDistance1, Float yDistance2) {
        //画中间的十字形
        canvas.drawLine(mHalfScreenWidth - centerSize,
                mHalfScreenHeight,
                mHalfScreenWidth + centerSize,
                mHalfScreenHeight,
                paint);
        canvas.drawLine(mHalfScreenWidth,
                mHalfScreenHeight - centerSize,
                mHalfScreenWidth,
                mHalfScreenHeight + centerSize,
                paint);


        //画2条ruler线
        canvas.drawLine(rulerPaddingX,
                yDistance1,
                mScreenWidth - rulerPaddingX,
                yDistance1,
                rulerPaint);

        canvas.drawCircle(mScreenWidth - spotRadius - rulerPaddingX, yDistance1, spotRadius, spotPaint);
        lastY1=yDistance1;



        canvas.drawLine(rulerPaddingX,
                yDistance2,
                mScreenWidth - rulerPaddingX,
                yDistance2,
                rulerPaint);
        canvas.drawCircle(mScreenWidth - spotRadius - rulerPaddingX, yDistance2, spotRadius, spotPaint);
        lastY2=yDistance2;


//        canvas.drawLine(rulerPaddingX * 2,
//                yDistance,
//                rulerPaddingX * 2,
//                mScreenHeight - yDistance,
//                rulerPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY=y;
                canY1= Math.abs(lastY1-y)<spotRadius;
                if (canY1)
                    canY2=false;
                else
                    canY2= Math.abs(lastY2-y)<spotRadius;
                break;
            case MotionEvent.ACTION_MOVE:
                if (canY1){
                    drawRulerLine(y-startY+lastY1,lastY2);
                }
                if (canY2){
                    drawRulerLine(lastY1,y-startY+lastY2);
                }
                startY=y;
//                Log.e(TAG, "MOVE :" + (y-startY));
                break;
            case MotionEvent.ACTION_UP:
//                Log.e(TAG, "UP :" + event.getX() + " , " + event.getY());
                break;
        }
//        invalidate();
        return true;
    }

    @Override
    public void run() {

    }

    /**
     * ruler高度变化回调接口
     */
    public interface OnRulerHeightChangedListener {
        void onRulerHeightChanged(float rulerHeight, Bitmap bitmap);
    }
}
