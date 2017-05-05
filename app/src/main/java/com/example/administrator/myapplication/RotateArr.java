package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/5/5.
 */

public class RotateArr extends View {

    private Paint mPaint ;
    private int radius = 150 ;

    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度

    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作
    private Path mPath;
    public RotateArr(Context context) {
        this(context,null);
    }

    public RotateArr(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RotateArr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.arr, options);
        pos = new float[2];
        tan = new float[2];
        mMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth()/2,getHeight()/2);
        mPath = new Path();
        mPath.addCircle(0,0,200, Path.Direction.CW) ;  //顺时针画个圆

        PathMeasure measure = new PathMeasure(mPath,false);
        measure.getPosTan(measure.getLength()*currentValue,pos,tan);

        mMatrix.reset();
        float degrees = (float) Math.toDegrees(Math.atan2(tan[1], tan[0]));
        Log.e(this.getClass().getSimpleName(),"degress ==" +degrees);
        mMatrix.postRotate(degrees, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        mMatrix.postTranslate(pos[0]-mBitmap.getWidth()/2, pos[1] - mBitmap.getHeight()/2 );   // 将图片绘制中心调整到与当前点重合
        canvas.drawPath(mPath, mPaint);                                   // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);                     // 绘制箭头
        currentValue += 0.005;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }
        invalidate();

//             postInvalidate();
    }
}
