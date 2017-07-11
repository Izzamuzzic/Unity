package com.zwq65.unity.ui.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zwq65.unity.R;


/**
 * Created by zwq65 on 2017/03/06.
 * 波浪view
 */
public class WaveBezierView extends View implements View.OnClickListener {

    private Paint mPaint, mPaint2;
    private Path mPath, mPath2;
    private int mWaveLength = 1000;
    private int mOffset;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mWaveCount;
    private int mCenterY;
    private ValueAnimator animator;
    private Boolean isPause = false;

    public WaveBezierView(Context context) {
        super(context);
    }

    public WaveBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaveBezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPath2 = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(context, R.color.blue));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(ContextCompat.getColor(context, R.color.light_blue));
        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);

        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        mScreenWidth = w;
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
        mCenterY = mScreenHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(-mWaveLength + mOffset, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            // + (i * mWaveLength)
            // + mOffset
            mPath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + 30, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mPath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - 30, i * mWaveLength + mOffset, mCenterY);
        }
        mPath.lineTo(mScreenWidth, mScreenHeight);//移动到屏幕的两个底角
        mPath.lineTo(0, mScreenHeight);
        mPath.close();//回到初始点
        canvas.drawPath(mPath, mPaint);


        mPath2.reset();
        mPath2.moveTo(-mWaveLength + mOffset, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            // + (i * mWaveLength)
            // + mOffset
            mPath2.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + 80, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mPath2.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - 80, i * mWaveLength + mOffset, mCenterY);
        }
        mPath2.lineTo(mScreenWidth, mScreenHeight);//移动到屏幕的两个底角
        mPath2.lineTo(0, mScreenHeight);
        mPath2.close();//回到初始点
        canvas.drawPath(mPath2, mPaint2);
    }

    @Override
    public void onClick(View view) {
        if (animator == null) {
            animator = ValueAnimator.ofInt(0, mWaveLength);
            animator.setDuration(2000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mOffset = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.start();
        } else {
            if (isPause) {
                isPause = false;
                animator.resume();
            } else {
                isPause = true;
                animator.pause();
            }

        }

    }
}