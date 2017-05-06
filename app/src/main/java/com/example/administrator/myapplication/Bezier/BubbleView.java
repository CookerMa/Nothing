package com.example.administrator.myapplication.Bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2017/5/6.
 */

public class BubbleView extends View {


    private static String TAG = "BubbleView";
    private Paint mPaint ;
    private int mCircleRadius = 20 ;  //小圆
    private int mBubbleRadius = 20; //拖拽气泡
    private float mCircleCenterX,mCircleCenterY,mBubbleCenterX,mBubbleCenterY,mControlX,mControlY,d ; //圆心距
    private STATE mState ;
    private int maxL = 6 * mBubbleRadius ;
    private Path mPath  = new Path();

    /* 黏连小圆的贝塞尔曲线起点横坐标 */
    private float mCircleStartX;
    /* 黏连小圆的贝塞尔曲线起点纵坐标 */
    private float mCircleStartY;
    /* 手指拖拽气泡的贝塞尔曲线终点横坐标 */
    private float mBubbleEndX;
    /* 手指拖拽气泡的贝塞尔曲线终点纵坐标 */
    private float mBubbleEndY;
    /* 手指拖拽气泡的贝塞尔曲线起点横坐标 */
    private float mBubbleStartX;
    /* 手指拖拽气泡的贝塞尔曲线起点纵坐标 */
    private float mBubbleStartY;
    /* 黏连小圆的贝塞尔曲线终点横坐标 */
    private float mCircleEndX;
    /* 黏连小圆的贝塞尔曲线终点纵坐标 */
    private float mCircleEndY;
    public BubbleView(Context context) {
        this(context,null);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaint();
        mState = STATE.NORMAL;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBubbleCenterX = mCircleCenterX = w/2;
        mBubbleCenterY = mCircleCenterY = h/2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mBubbleCenterX = mCircleCenterX = getWidth()/2;
        mBubbleCenterY = mCircleCenterY = getHeight()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

          if (!mState.equals(STATE.DISMISS))
        canvas.drawCircle(mBubbleCenterX,mBubbleCenterY,mBubbleRadius,mPaint);
        if (mState.equals(STATE.DARG) && d < maxL-maxL/4)   //画贝塞尔曲线
        {
            canvas.drawCircle(mCircleCenterX,mCircleCenterY,mCircleRadius,mPaint);         //画固定的小球

            //计算控制点坐标，为两圆圆心连线的中点
            mControlX = (mBubbleCenterX + mCircleCenterX) / 2;
            mControlY = (mBubbleCenterY + mCircleCenterY) / 2;
            //计算两条二阶贝塞尔曲线的起点和终点
            float sin = (mBubbleCenterY - mCircleCenterY) / d;
            float cos = (mBubbleCenterX - mCircleCenterX) / d;
            Log.e(TAG,(mBubbleCenterY - mCircleCenterY)+"|---------------|"+d);
            mCircleStartX = mCircleCenterX - mCircleRadius * sin;
            mCircleStartY = mCircleCenterY + mCircleRadius * cos;
            mBubbleEndX = mBubbleCenterX - mBubbleRadius * sin;
            mBubbleEndY = mBubbleCenterY + mBubbleRadius * cos;
            mBubbleStartX = mBubbleCenterX + mBubbleRadius * sin;
            mBubbleStartY = mBubbleCenterY - mBubbleRadius * cos;
            mCircleEndX = mCircleCenterX + mCircleRadius * sin;
            mCircleEndY = mCircleCenterY - mCircleRadius * cos;
            //画二阶贝赛尔曲线
            mPath.reset();
            mPath.moveTo(mCircleStartX, mCircleStartY);
            mPath.quadTo(mControlX, mControlY, mBubbleEndX, mBubbleEndY);
            mPath.lineTo(mBubbleStartX, mBubbleStartY);
            mPath.quadTo(mControlX, mControlY, mCircleEndX, mCircleEndY);
            mPath.close();
            canvas.drawPath(mPath, mPaint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (!mState.equals(STATE.DISMISS))  //判断是否能拖拽
                {
                    double d = Math.hypot(event.getX() - mBubbleCenterX, mBubbleRadius);
                    if (d < mBubbleRadius+maxL/4)
                        mState = STATE.DARG ;
                    else
                        mState = STATE.NORMAL;
                }
                Log.e(TAG,"state =  "+ mState.name());
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mState.equals(STATE.NORMAL))
                {

                    mBubbleCenterY = (int) event.getY();
                    mBubbleCenterX = (int) (event.getX());

                    d  = (float) Math.hypot(mBubbleCenterX - mCircleCenterX, mBubbleCenterY - mCircleCenterY);  //圆心距

                    if (mState.equals(STATE.DARG)) //可拖拽状态
                    {
                     if (d < maxL -maxL/4) //在最大拖动范围内
                     {
                         mCircleRadius  = (int) (mBubbleRadius - d/8);
                     }else
                     {
                         mState = STATE.MOVE;//改为移动状态
                     }

                    }

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:

                if (mState .equals(STATE.DARG)) {//正在拖拽时松开手指，气泡恢复原来位置并颤动一下
                    setBubbleRestoreAnim();
                } else if (mState .equals(STATE.MOVE)) {//正在移动时松开手指
                    //如果在移动状态下间距回到两倍半径之内，我们认为用户不想取消该气泡
                    if (d < 2 * mBubbleRadius) {//那么气泡恢复原来位置并颤动一下
                        setBubbleRestoreAnim();
                    } else {//气泡消失
                        setBubbleDismissAnim();
                    }
                }
//                mCircleRadius = mBubbleRadius;
                break;
        }

        return true;
    }


    /**
     * 设置气泡复原的动画
     */



    class PointFEvaluator implements TypeEvaluator<PointF>
    {

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float x = startValue.x + (fraction * (endValue.x - startValue.x));
            float y = startValue.y + (fraction * (endValue.y - startValue.y));

            return new PointF(x, y);
        }
    }
    private void setBubbleRestoreAnim() {
        ValueAnimator anim = ValueAnimator.ofObject(new PointFEvaluator(),
                new PointF(mBubbleCenterX, mBubbleCenterY),
                new PointF(mCircleCenterX, mCircleCenterY));
        anim.setDuration(500);
        //自定义Interpolator差值器达到颤动效果
        anim.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                float f = 0.571429f;
                return (float) (Math.pow(2, -4 * input) * Math.sin((input - f / 4) * (2 * Math.PI) / f) + 1);
            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF curPoint = (PointF) animation.getAnimatedValue();
                mBubbleCenterX = curPoint.x;
                mBubbleCenterY = curPoint.y;
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后状态改为默认
                mState = STATE.NORMAL;
            }
        });
        anim.start();
    }

    /**
     * 设置气泡消失的动画
     */
    private void setBubbleDismissAnim() {
        mState = STATE.DISMISS;//气泡改为消失状态
//        mIsExplosionAnimStart = true;
//        //做一个int型属性动画，从0开始，到气泡爆炸图片数组个数结束
//        ValueAnimator anim = ValueAnimator.ofInt(0, mExplosionDrawables.length);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setDuration(500);
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                //拿到当前的值并重绘
//                mCurExplosionIndex = (int) animation.getAnimatedValue();
//                invalidate();
//            }
//        });
//        anim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                //动画结束后改变状态
//                mIsExplosionAnimStart = false;
//            }
//        });
//        anim.start();
    }




    //正常状态，拖拽，消失，超过
    enum STATE
    {
        NORMAL,DARG,DISMISS,MOVE
    }
    private void setPaint() {
        mPaint  = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
    }


}
