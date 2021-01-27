package com.feisukj.cleaning.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

public class WaveView extends View {
    private Paint mPaint;

    // view宽度
    private int width;
    // view高度
    private int height;

    // 波浪高低偏移量
    private int offset = 20;

    // X轴，view的偏移量
    private int xoffset = 0;

    // view的Y轴高度
    private int viewY = 0;

    // 波浪速度
    private int waveSpeed = 400;

    private ValueAnimator animator;

    public WaveView(Context context) {
        super(context);
        init(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        mPaint = new Paint();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        width=size.x;
        height=size.y;

        animator = new ValueAnimator();
        animator.setFloatValues(0, width);
        animator.setDuration(waveSpeed * 20);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float change = (float) animation.getAnimatedValue();
                xoffset = (int) change;
                createShader();
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //设置一个默认值，就是这个View的默认宽度为500，这个看我们自定义View的要求
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        width = result;
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        height = specSize;
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawRect(new Rect(200, 0, width - 200, height), mPaint);
        canvas.drawPaint(mPaint);
        if (bitmap!=null){
            bitmap.recycle();
            bitmap=null;
        }
    }

    private Bitmap bitmap;
    private void createShader() {

        if (width<=0||height<=0){
            return;
        }
        bitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        Path path = new Path();

        viewY = height / 2;
        offset=viewY;//
        // 绘制屏幕外的波浪
        path.moveTo(xoffset - width, viewY);
        path.quadTo(width / 4 + xoffset - width, viewY - offset, width / 2 + xoffset - width, viewY);
        path.moveTo(width / 2 + xoffset - width, viewY);
        path.quadTo(width / 4 * 3 + xoffset - width, viewY + offset, width + xoffset - width, viewY);

        // 绘制屏幕内的波浪
        path.moveTo(xoffset, viewY);
        path.quadTo(width / 4 + xoffset, viewY - offset, width / 2 + xoffset, viewY);
        path.moveTo(width / 2 + xoffset, viewY);
        path.quadTo(width / 4 * 3 + xoffset, viewY + offset, width + xoffset, viewY);

        // 新增了这里
        path.lineTo(width + xoffset, height);
        path.lineTo(xoffset - width, height);
        path.lineTo(xoffset - width, viewY);

        canvas.drawPath(path, paint);

        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mPaint.setShader(bitmapShader);

    }

    public void startAnimation(){
        animator.start();
    }

    public void stopAnimation(){
        if (animator!=null)
            animator.cancel();
    }
}
