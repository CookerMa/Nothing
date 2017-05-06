package com.example.administrator.myapplication.Paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.myapplication.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 刮刮卡
 * 全部省略自定义属性
 * 保存的bitmap是没有背景的
 */

public class LotteryCard extends View {


    private Paint mPaint ,pathPaint;
    private Bitmap mBackBtimap ;
    private  Canvas mCanvas ;
    private Bitmap bitmap;
    private int mWidth ,mHeight ,mLastX,mLastY;
    private boolean isMeasure ;

    private Path mPath ;
    public LotteryCard(Context context) {
        this(context,null);
    }

    public LotteryCard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LotteryCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaint();
        mPath = new Path();
//        BitmapFactory.Options option = new BitmapFactory.Options();
//        option.inSampleSize = 2;
        mBackBtimap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasure)
        {
            isMeasure = !isMeasure ;
            bitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(bitmap);
//            mCanvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), pathPaint); //添加背景色
            mCanvas.drawColor(Color.parseColor("#808080"));
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void setPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#808080"));

        pathPaint = new Paint();
        pathPaint.setStrokeWidth(20);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setColor(Color.parseColor("#21a675"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        mCanvas.drawRect(0,0,mWidth,mHeight,mPaint);


        canvas.drawBitmap(mBackBtimap,0,0,null);
        pathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath,pathPaint);
        canvas.drawBitmap(bitmap,0,0,null);
    }

    public void saveBitmap() {
        Log.e("TTT","save start");
        File f = new File(Environment.getExternalStorageDirectory(), "drawpic.png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.e("TTT","save success");
        } catch (Exception e) {
            Log.e("TTT","save exp ="+e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                mPath.moveTo(event.getX(),event.getY());

                break;
            case MotionEvent.ACTION_MOVE:

                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if (dx > 3 || dy > 3)
                    mPath.lineTo(x, y);

                mLastX = x;
                mLastY = y;
                break;
        }

        invalidate();
        return super.onTouchEvent(event);
    }
}
