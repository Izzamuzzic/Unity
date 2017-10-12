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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jingewenku.abrahamcaijin.commonutil.DensityUtils;
import com.zwq65.unity.R;

/**
 * ================================================
 * 自定义view（仿百度贴吧加载球）
 * <p>
 * Created by NIRVANA on 2017/07/13.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class BaiduLoadingView extends View {

    private int mHeight = DensityUtils.dip2px(getContext(), 50);
    private int mWidth = DensityUtils.dip2px(getContext(), 50);
    private Path path;
    private Path o;
    private Paint textPaint;
    private Paint mPaint;
    private float currentPercent;
    private int color;
    private String text = "贴";

    public BaiduLoadingView(Context context) {
        super(context);
    }

    public BaiduLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaiduLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BaiduLoadingView);
        //自定义颜色和文字
        color = array.getColor(R.styleable.BaiduLoadingView_color, Color.rgb(41, 163, 254));
        text = array.getString(R.styleable.BaiduLoadingView_text);
        array.recycle();

        //图形及路径填充画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        mPaint.setDither(true);
        //文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        //闭合波浪路径
        path = new Path();
        //圆形外围路径
        o = new Path();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            currentPercent = animation.getAnimatedFraction();
            invalidate();
        });
        valueAnimator.start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.EXACTLY == widthMode) {
            mWidth = width;
        }
        if (MeasureSpec.EXACTLY == heightMode) {
            mHeight = height;
        }
        setMeasuredDimension(mWidth, mHeight);
        int textSize = mWidth / 2;
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制波浪上方的文字
        textPaint.setColor(color);
        drawCenterText(canvas, textPaint, text);
        o.addCircle(mWidth / 2, mHeight / 2, mWidth / 2, Path.Direction.CCW);
        canvas.clipPath(o);
        path = getActionPath(currentPercent);
        canvas.drawPath(path, mPaint);
        canvas.clipPath(path);
        textPaint.setColor(Color.WHITE);
        drawCenterText(canvas, textPaint, text);
    }

    private Path getActionPath(float percent) {
        Path path = new Path();
        int x = -mWidth;
        //当前x点坐标（根据动画进度水平推移，一个动画周期推移的距离为一个mWidth）
        x += percent * mWidth;
        //波形的起点
        path.moveTo(x, mHeight / 2);
        //控制点的相对宽度
        int quadWidth = mWidth / 4;
        //控制点的相对高度
        int quadHeight = mHeight / 20 * 3;
        //第一个周期
        path.rQuadTo(quadWidth, quadHeight, quadWidth * 2, 0);
        path.rQuadTo(quadWidth, -quadHeight, quadWidth * 2, 0);
        //第二个周期
        path.rQuadTo(quadWidth, quadHeight, quadWidth * 2, 0);
        path.rQuadTo(quadWidth, -quadHeight, quadWidth * 2, 0);
        //右侧的直线
        path.lineTo(x + mWidth * 2, mHeight);
        //下边的直线
        path.lineTo(x, mHeight);
        //自动闭合补出左边的直线
        path.close();
        return path;
    }

    private void drawCenterText(Canvas canvas, Paint textPaint, String text) {
        Rect rect = new Rect(0, 0, mWidth, mHeight);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        //文字框最高点距离baseline的距离（负数）
        float top = fontMetrics.top;
        //文字框最低点距离baseline的距离（正数）
        float bottom = fontMetrics.bottom;
        //调整到正中位置
        int centerY = (int) (rect.centerY() - top / 2 - bottom / 2);

        canvas.drawText(text, rect.centerX(), centerY, textPaint);

    }
}
