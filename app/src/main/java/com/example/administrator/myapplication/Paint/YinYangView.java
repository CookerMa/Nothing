package com.example.administrator.myapplication.Paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 太极图，实现很简单
 * （1）画圆
 *（2）用path.op
 */

public class YinYangView extends View {

    private Paint blackPaint ;
    private Paint whitePaint ;
    private Bitmap bitmap ;
    private Canvas mCanvas ;
private int radius = 100 ;

    public YinYangView(Context context) {
        this(context,null);
    }

    public YinYangView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YinYangView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        bitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
    }

    private void setPaint() {
        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        mCanvas.drawColor(Color.GRAY);

        mCanvas.translate(getWidth()/2,getHeight()/2);
        RectF rect = new RectF(-radius, -radius, radius, radius);
        RectF small1 = new RectF(-radius/2, -radius, radius/2, 0);
        RectF small2 = new RectF(-radius/2, 0, radius/2, radius);
        mCanvas.drawArc(rect, 90, 180, true, blackPaint);            //绘制黑色半圆
        mCanvas.drawArc(rect, -90, 180, true, whitePaint);

        mCanvas.drawArc(small1, -90, 180, true, blackPaint);
        mCanvas.drawArc(small2, 90, 180, true, whitePaint);

        canvas.drawBitmap(bitmap,0,0,null);

    }
}
