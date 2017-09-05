package com.zwq65.unity.utils;

import android.widget.Toast;

import com.zwq65.unity.app.App;

/**
 * Created by zwq65 on 2017/05/08.
 * 防止多次调用弹出toast
 */

public class ToastUtils {
    private static Toast toast;

    public static void makeText(String message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(), message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        toast.show();
    }
}
