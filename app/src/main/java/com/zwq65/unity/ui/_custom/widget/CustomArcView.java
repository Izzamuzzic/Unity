/*
 *    Copyright [2017] [NIRVANA PRIVATE LIMITED]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zwq65.unity.ui._custom.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomArcView extends View {
    private Paint arcPaint1;//顶部弧形画笔
    private Paint arcPaint2;//底部弧形画笔
    private RectF rectF;//弧形外接矩形
    private int radius;//弧形的半径
    private float result = 0;//滑过弧形的弧度
    private float mStartAngle = 150;//弧形起点弧度
    private float mEndAngle = 240;//弧形结束弧度

    public CustomArcView(Context context) {
        super(context);
        init();
    }

    public CustomArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int defaultSize = 600;

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultSize, defaultSize);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultSize, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultSize);
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF = new RectF();
        //获取弧形的半径大小
        radius = (int) (Math.min(getWidth(), getHeight()) / 2 - arcPaint1.getStrokeWidth()) - 100;
        rectF.left = getWidth() / 2 - radius;
        rectF.top = getHeight() / 2 - radius;
        rectF.right = getWidth() / 2 + radius;
        rectF.bottom = getHeight() / 2 + radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图
        //绘制弧形
        canvas.drawArc(rectF, mStartAngle, mEndAngle, false, arcPaint2);
        canvas.save();
        canvas.drawArc(rectF, mStartAngle, getResult(), false, arcPaint1);
        canvas.restore();
    }

    private void init() {
        arcPaint1 = new Paint();
        arcPaint2 = new Paint();
        initArcPaint(arcPaint1);
        initArcPaint(arcPaint2);
        arcPaint1.setColor(Color.parseColor("#7CCD7C"));
        arcPaint2.setColor(Color.parseColor("#33000000"));
    }

    /**
     * 初始化弧形画笔的样式
     *
     * @param arcPaint
     */
    private void initArcPaint(Paint arcPaint) {
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(20);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, (result * mEndAngle) / 100);
        valueAnimator.setDuration(2000);
        valueAnimator.setTarget(this);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAnim(animation);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setResult(80);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void startAnim(ValueAnimator animation) {
        result = (float) animation.getAnimatedValue();
        invalidate();
    }
}
