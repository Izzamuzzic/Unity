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
    private static final int POINT_NUM = 50;
    private int mWidth, mHeight;
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
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}
