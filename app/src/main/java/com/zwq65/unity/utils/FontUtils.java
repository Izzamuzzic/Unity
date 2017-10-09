package com.zwq65.unity.utils;

import android.graphics.Typeface;
import android.widget.TextView;


/**
 * Created by zwq65 on 2017/08/10
 */

public class FontUtils {
    private static volatile FontUtils instance;

    public enum Font {
        //方正宋刻本秀楷简体
        FZSongKeBenXiuKai,
        //方正悠黑_504L
        FZYouH_504L,
        //方正悠黑_508R
        FZYouH_508R
    }

    public static FontUtils getInstance() {
        if (instance == null) {
            synchronized (FontUtils.class) {
                if (instance == null) {
                    instance = new FontUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 为textView设置字体
     *
     * @param textView textView
     * @param font     font
     */
    public void setTypeface(TextView textView, Font font) {
        Typeface typeface = null;
        switch (font) {
            case FZSongKeBenXiuKai:
                typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/FZSongKeBenXiuKai.TTF");
                break;
            case FZYouH_504L:
                typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/FZYouH_504L.otf");
                break;
            case FZYouH_508R:
                typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/FZYouH_508R.ttf");
                break;
            default:
                break;
        }
        if (typeface != null) {
            textView.setTypeface(typeface);
        }
    }

}
