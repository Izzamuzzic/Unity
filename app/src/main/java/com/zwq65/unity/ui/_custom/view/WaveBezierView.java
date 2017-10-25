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

package com.zwq65.unity.ui._custom.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zwq65.unity.R;

/**
 * ================================================
 * 波浪view
 * <p>
 * Created by NIRVANA on 2017/03/06
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class WaveBezierView extends View {

    private Paint mPaint, mPaint2;
    private Path mPath, mPath2;
    private float currPercent;
    private int mScreenHeight;
    private int mScreenWidth;
    private ValueAnimator animator;

    public WaveBezierView(Context context) {
        super(context);
    }

    public WaveBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaveBezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        startAnim();
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setStyle(Paint.Style.FILL_AND_STROKE);

        Shader shader1 = new LinearGradient(0, 0, 100, 100, ContextCompat.getColor(context, R.color.wave2),
                ContextCompat.getColor(context, R.color.wave1), Shader.TileMode.CLAMP);
        mPaint.setShader(shader1);
        mPaint2.setShader(shader1);
    }

    private void startAnim() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(4000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            currPercent = (float) animation.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        mScreenWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath = getWavePath1(currPercent);
        canvas.drawPath(mPath, mPaint);
        mPath2 = getWavePath2(currPercent);
        canvas.drawPath(mPath2, mPaint2);

    }

    private Path getWavePath1(float percent) {
        Path path = new Path();
        int x = -mScreenWidth;
        //当前x点坐标（根据动画进度水平推移，一个动画周期推移的距离为一个mWidth）
        x += percent * mScreenWidth;
        //设置起点
        path.moveTo(x, mScreenHeight / 3);
        //控制点的相对宽度
        int quadWidth = mScreenWidth / 4;
        //控制点的相对高度
        int quadHeight = mScreenHeight / 5;
        //相对起点绘制贝塞尔曲线（rQuadTo():相对path起点坐标绘制）
        //第一个周期
        path.rQuadTo(quadWidth, quadHeight, 2 * quadWidth, 0);
        path.rQuadTo(quadWidth, -quadHeight, 2 * quadWidth, 0);
        //第二个周期
        path.rQuadTo(quadWidth, quadHeight, 2 * quadWidth, 0);
        path.rQuadTo(quadWidth, -quadHeight, 2 * quadWidth, 0);
        //右侧的直线
        path.lineTo(x + mScreenWidth * 2, mScreenHeight);
        //下边的直线
        path.lineTo(x, mScreenHeight);
        //自动闭合补出左边的直线
        path.close();
        return path;
    }

    private Path getWavePath2(float percent) {
        Path path = new Path();
        int x = -2 * mScreenWidth;
        //当前x点坐标（根据动画进度水平推移，一个动画周期推移的距离为一个mWidth）
        x += percent * 2 * mScreenWidth;
        //设置起点
        path.moveTo(x, mScreenHeight / 3);
        //控制点的相对宽度
        int quadWidth = mScreenWidth / 2;
        //控制点的相对高度
        int quadHeight = mScreenHeight / 6;
        //相对起点绘制贝塞尔曲线（rQuadTo():相对path起点坐标绘制）
        //第一个周期
        path.rQuadTo(quadWidth, -quadHeight, 2 * quadWidth, 0);
        path.rQuadTo(quadWidth, quadHeight, 2 * quadWidth, 0);
        //第二个周期
        path.rQuadTo(quadWidth, -quadHeight, 2 * quadWidth, 0);
//        path.rQuadTo(quadWidth, quadHeight, 2 * quadWidth, 0);
        //右侧的直线
        path.lineTo(x + mScreenWidth * 3, mScreenHeight);
        //下边的直线
        path.lineTo(x, mScreenHeight);
        //自动闭合补出左边的直线
        path.close();
        return path;
    }
}