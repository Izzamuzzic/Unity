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
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zwq65.unity.R;

/**
 * ================================================
 * 自定义字体格式的TextView
 * <p>
 * Created by NIRVANA on 2017/10/10
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Deprecated
public class FontTextView extends AppCompatTextView {
    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context, attrs);
    }

    /**
     * 替换字体格式
     *
     * @param attrs AttributeSet
     */
    private void setFont(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        int fontAttrs = typedArray.getInt(R.styleable.FontTextView_fonts, 1);
        typedArray.recycle();
        String customFont;
        switch (fontAttrs) {
            case 1:
                customFont = "fonts/FZSongKeBenXiuKai.TTF";
                break;
            case 2:
                customFont = "fonts/FZYouH_504L.otf";
                break;
            default:
                customFont = "fonts/FZYouH_504L.otf";
                break;
        }
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), customFont);
        setTypeface(typeface);
    }

}
