package com.zwq65.unity.utils;

import android.graphics.Typeface;
import android.widget.TextView;


/**
 * Created by zwq65 on 2017/08/10
 */

public class FontUtils {
    private static volatile FontUtils instance;

    public enum Font {
        Montserrat_Medium,
        Roboto_Medium,
        Roboto_Bold
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
            case Montserrat_Medium:
                typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/Montserrat-Medium.ttf");
                break;
            case Roboto_Medium:
                typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/Roboto-Medium.ttf");
                break;
            case Roboto_Bold:
                typeface = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/Roboto-Bold.ttf");
                break;
            default:
                break;
        }
        if (typeface != null) {
            textView.setTypeface(typeface);
        }
    }

}
