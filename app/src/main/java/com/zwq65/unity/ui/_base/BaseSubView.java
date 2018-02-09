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

package com.zwq65.unity.ui._base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * ================================================
 * 自定义view基类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public abstract class BaseSubView extends View {
    public final String TAG = getClass().getSimpleName();

    public Context mContext;
    /**
     * 自定义view的宽度和高度
     */
    public int mWidth, mHeight;

    /**
     * 自定义view的中心坐标
     */
    public int centerX, centerY;

    public BaseSubView(Context context) {
        this(context, null);
    }

    public BaseSubView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSubView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setUp(context, attrs);
    }

    /**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w    Current width of this view.
     * @param h    Current height of this view.
     * @param oldw Old width of this view.
     * @param oldh Old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        centerX = w / 2;
        centerY = h / 2;
    }

    /**
     * 自定义view初始化成员变量和获取attrs的value
     *
     * @param context context
     * @param attrs   attrs
     */
    protected abstract void setUp(Context context, @Nullable AttributeSet attrs);
}
