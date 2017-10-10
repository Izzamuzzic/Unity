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
        String customFont = "fonts/FZYouH_504L.otf";
        switch (fontAttrs) {
            case 1:
                customFont = "fonts/FZSongKeBenXiuKai.TTF";
                break;
            case 2:
                customFont = "fonts/FZYouH_504L.otf";
                break;
            case 3:
                customFont = "fonts/FZYouH_508R.ttf";
                break;
        }
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), customFont);
        setTypeface(typeface);
    }

}
