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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.jingewenku.abrahamcaijin.commonutil.DensityUtils;
import com.zwq65.unity.R;
import com.zwq65.unity.utils.LogUtils;

/**
 * ================================================
 * 自定义表格
 * <p>
 * Created by NIRVANA on 2017/09/07
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class MyChartView extends View {

    private Context context;
    private SparseArray<Float> array1, array2;
    private int max = 70;
    private int colorRed, colorBlue;
    private int mHeight = DensityUtils.dip2px(getContext(), 200);
    private int mWidth = DensityUtils.dip2px(getContext(), 300);
    private Paint linePaint, textPaint, redPaint, bluePaint;
    private Path mPath;

    public MyChartView(Context context) {
        this(context, null);
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initData();
        init(attrs);
    }

    private void initData() {
        array1 = new SparseArray<>();
        array2 = new SparseArray<>();
        array1.put(1, 30f);
        array1.put(2, 15f);
        array1.put(3, 50f);
        array1.put(4, 50f);
        array1.put(5, 30f);
        array1.put(6, 25f);
        array1.put(7, 60f);

        array2.put(1, 10f);
        array2.put(2, 25f);
        array2.put(3, 20f);
        array2.put(4, 25f);
        array2.put(5, 50f);
        array2.put(6, 65f);
        array2.put(7, 25f);
    }

    private void init(AttributeSet attrs) {
        colorRed = ContextCompat.getColor(context, R.color.chart_red);
        colorBlue = ContextCompat.getColor(context, R.color.chart_blue);
        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setStrokeCap(Paint.Cap.ROUND);
        bluePaint.setStrokeCap(Paint.Cap.ROUND);
        redPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStyle(Paint.Style.STROKE);

        //设置阴影
        bluePaint.setShader(new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight, colorBlue,
                ContextCompat.getColor(context, R.color.chart_blue_trans), Shader.TileMode.CLAMP));
        redPaint.setShader(new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight, colorRed,
                ContextCompat.getColor(context, R.color.chart_red_trans), Shader.TileMode.CLAMP));

        //设置宽度
        redPaint.setStrokeWidth(DensityUtils.dip2px(getContext(), 3));
        bluePaint.setStrokeWidth(DensityUtils.dip2px(getContext(), 3));
        linePaint.setStrokeWidth(DensityUtils.dip2px(getContext(), 1));
        //设置颜色
        linePaint.setColor(ContextCompat.getColor(context, R.color.chart_line_color));
        redPaint.setColor(colorRed);
        bluePaint.setColor(colorBlue);
        textPaint.setColor(ContextCompat.getColor(context, R.color.chart_text_color));
        textPaint.setTextSize(18f);
        //设置虚线模式
        linePaint.setPathEffect(new DashPathEffect(new float[]{2, 2}, 0));
        //设置圆角模式
        redPaint.setPathEffect(new CornerPathEffect(90));
        bluePaint.setPathEffect(new CornerPathEffect(90));
        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawDashLine(0f, canvas);
        drawDashLine(10f, canvas);
        drawDashLine(20f, canvas);
        drawDashLine(30f, canvas);
        drawDashLine(40f, canvas);
        drawDashLine(50f, canvas);
        drawDashLine(60f, canvas);
        drawDashLine(70f, canvas);
        linePath(array1, canvas, bluePaint);
        linePath(array2, canvas, redPaint);
    }

    private void linePath(SparseArray<Float> array, Canvas canvas, Paint paint) {
        mPath.reset();
        mPath.moveTo(0, mHeight);
        mPath.lineTo(0, getmHeight(array.get(1)));
        int size = array.size();
        float x, y;
        for (int i = 2; i < size + 1; i++) {
            x = (i - 1) * (mWidth / (size - 1));
            y = getmHeight(array.get(i));
            mPath.lineTo(x, y);
//            mPath.addCircle(x, y, DensityUtils.dip2px(getContext(), 3), Path.Direction.CCW);
//            canvas.drawCircle(x, y, DensityUtils.dip2px(getContext(), 2), paint);
            LogUtils.i("linePath", "x:" + x + "  y:" + y);
        }
        mPath.rLineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, paint);
    }

    private void drawDashLine(float height, Canvas canvas) {
        float y = getmHeight(height);
        canvas.drawText(String.valueOf((int) height), 0, y, textPaint);
        mPath.reset();
        mPath.moveTo(0, y);
        mPath.rLineTo(mWidth, 0);
        canvas.drawPath(mPath, linePaint);
    }

    private float getmHeight(float num) {
        return ((max - num) / max) * mHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (MeasureSpec.EXACTLY == widthMode) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
//        if (MeasureSpec.EXACTLY == heightMode) {
//            mHeight = MeasureSpec.getSize(heightMeasureSpec);
//        }
        mHeight = (mWidth * 2) / 3;
        LogUtils.i("mWidth:" + mWidth + " mHeight" + mHeight);
        setMeasuredDimension(mWidth, mHeight);
    }
}
