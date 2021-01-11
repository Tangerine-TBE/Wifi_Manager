package com.example.module_tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.module_tool.R;


public class CycleRulerView extends View {
    //测量角度
    private int kedu;
    //view的宽度
    private int width;
    //view的高度
    private int height;
    //view的半径
    private float radius;
    private float padding;
    //view刻度字体大小
    private float fontSize;
    private float offset;
    //触摸点
    private Point point;
    //显示测量角度结果文字大小
    private float DISPLAY_SIZE;
    //显示测量角度结果文字颜色
    private int resultTextColor;
    //刻度文字颜色
    private int markTextColor;
    //view边框颜色
    private int bolderColor;
    //刻度颜色
    private int markColor;
    private int color = getResources().getColor(R.color.light_green);

    public CycleRulerView(Context context) {
        this(context, null);
    }

    public CycleRulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CycleRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CycleRulerView);
        resultTextColor = a.getColor(R.styleable.CycleRulerView_resultTextColor, color);
        markTextColor = a.getColor(R.styleable.CycleRulerView_markTextColor, color);
        bolderColor = a.getColor(R.styleable.CycleRulerView_bolderColor, color);
        markColor = a.getColor(R.styleable.CycleRulerView_markColor, color);
        a.recycle();
    }

    private void init(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        padding = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, dm);
        //转换成手机识别的像素单位
        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11,
                dm);
        DISPLAY_SIZE = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40, dm);
    }

    class Point {
        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "[x:" + x + ", y:" + y + "]";
        }
    }

    /**
     * 获取文字绘制路径
     *
     * @param text
     * @param paint
     * @param degree
     * @param r
     * @return
     */
    private Path getTextPath(String text, Paint paint, double degree, float r) {
        double pathDegree = Math.abs(90 - degree);
        float textWidth = paint.measureText(text);
        float y = Math.abs((float) (textWidth * Math.sin(pathDegree / 180
                * Math.PI)));
        float x = Math.abs((float) (textWidth * Math.cos(pathDegree / 180
                * Math.PI)));
        Point point = getPoint(r, degree);
        Point start = new Point();
        Point end = new Point();
        if (degree < 90) {
            end.setX(-point.getX() + x / 2);
            end.setY(-point.getY() - y / 2);
            start.setX(-point.getX() - x / 2);
            start.setY(-point.getY() + y / 2);
        } else {
            end.setX(-point.getX() + x / 2);
            end.setY(-point.getY() + y / 2);
            start.setX(-point.getX() - x / 2);
            start.setY(-point.getY() - y / 2);
        }
        Path path = new Path();
        path.moveTo(start.getX(), start.getY());
        path.lineTo(end.getX(), end.getY());
        return path;
    }

    /**
     * 显示测量结果
     *
     * @param canvas
     */
    private void drawDisplay(Canvas canvas) {
        String cm = String.valueOf(kedu);
        Paint displayPaint1 = new Paint();
        displayPaint1.setAntiAlias(true);
        displayPaint1.setColor(resultTextColor);
        displayPaint1.setTextSize(DISPLAY_SIZE);
        float cmWidth = displayPaint1.measureText(cm);
        Rect bounds1 = new Rect();
        displayPaint1.getTextBounds(cm, 0, cm.length(), bounds1);
        canvas.drawText(cm + "°", width / 2 - cmWidth / 2,
                height * 3 / 5 + bounds1.height() / 6, displayPaint1);
    }

    private Point getPoint(float r, double degree) {
        float x = (float) (r * Math.cos(degree / 180 * Math.PI));
        float y = (float) (r * Math.sin(degree / 180 * Math.PI));
        return new Point(x, y);
    }

    private void onTouchBegin(Point point) {
        caculatePoint(point);
    }

    private void onTouchMove(Point point) {
        caculatePoint(point);
    }


    private void caculatePoint(Point point) {
        float mx = width / 2f;
        float my = height - offset;
        if (point.getY() > my) {
            point.setY(my);
        }
        float dx = point.getX() - mx;
        float dy = point.getY() - my;
        double r = Math.sqrt(dx * dx + dy * dy);
        float x = (float) (dx / r * radius);
        float y = (float) (dy / r * radius);
        this.point = new Point(x, y);
        kedu = (int) Math.round(Math.atan(dy / dx) / Math.PI * 180);
        if (dx >= 0) {
            kedu += 180;
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(new Point(event.getX(), event.getY()));
                break;
            case MotionEvent.ACTION_DOWN:
                onTouchBegin(new Point(event.getX(), event.getY()));
                break;
        }
        return true;
    }

    Paint paint = new Paint();
    Paint xpaint = new Paint();
    Paint paint2 = new Paint();
    Paint degreePaint = new Paint();
    Paint arcPaint = new Paint();
    RectF oval1 = new RectF();
    RectF oval = new RectF(0, offset, width, width + offset);
    PorterDuffXfermode porterDuffXfermode=new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    RectF rectF=new RectF(0, 0, width, height);
    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        radius = width / 2f;
        paint.setColor(0x00000000);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(15f);
        offset = (height - (width / 2f)) / 2f;
        oval.top=offset;
        oval.right=width;
        oval.bottom=width+offset;
        canvas.drawArc(oval, 180, 180, true, paint);//绘制圆弧
//        xpaint.setAntiAlias(true);
//        xpaint.setXfermode(porterDuffXfermode);//混合模式
//        xpaint.setStyle(Paint.Style.FILL);
//        rectF.right=width;
//        rectF.bottom=height;
//        canvas.drawRect(rectF, xpaint);
        canvas.save();
        canvas.translate(width / 2, height - offset);//画布平移
        paint2.setAntiAlias(true);
        paint2.setColor(markColor);//刻度颜色
        paint2.setStrokeWidth(2);//设置线宽度
        degreePaint.setAntiAlias(true);
        degreePaint.setTextSize(fontSize);
        degreePaint.setColor(markTextColor);//刻度盘角度文字颜色
        for (int i = 1; i < 180; i++) {
            Point point = getPoint(radius, i);
            float x = point.getX();
            float y = point.getY();
            float r = radius - padding / 2;
            if ((i % 5) == 0) {
                if ((i & 0x1) == 0) {
                    // 10
                    r = radius - padding;
                    String text = String.valueOf((int) (i));
                    Path path = getTextPath(text, degreePaint, i, radius
                            - padding - fontSize * 5 / 4);
                    canvas.drawTextOnPath(text, path, 0, 0, degreePaint);//根据path绘制文字
                } else {
                    // 5
                    r = radius - padding * 3 / 4;
                }
            }
            Point point1 = getPoint(r, i);
            float x1 = point1.getX();
            float y1 = point1.getY();
            canvas.drawLine(-x1, -y1, -x, -y, paint2);//绘制刻度线
        }
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(bolderColor);//刻度盘边框颜色
        arcPaint.setStrokeWidth(1);
        arcPaint.setStyle(Paint.Style.STROKE);
        oval1.left = -width / 2f;
        oval1.top = offset * 2f - height;
        oval1.right = width / 2f;
        oval1.bottom = height - offset * 2f;
        canvas.drawArc(oval1, 180, 180, true, arcPaint);
        canvas.drawLine(0, 0, 0, -padding, paint2);//绘制中心参考线
        if (point != null) {
            canvas.drawLine(0, 0, point.getX(), point.getY(), paint2);
        }
        canvas.restore();
        drawDisplay(canvas);
    }
}
