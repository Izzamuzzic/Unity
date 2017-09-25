package com.zwq65.unity.ui._custom.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * ================================================
 * <p> 随机生成的点连线漂浮移动,类似知乎登录背景
 * Created by NIRVANA on 2017/09/25
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class RandomPointLineView extends View {

    private Paint mPaint;
    private List<Integer> points;

    public RandomPointLineView(Context context) {
        this(context, null);
    }

    public RandomPointLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomPointLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }
}
