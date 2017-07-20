package com.zwq65.unity.utils;


import android.util.Log;

import com.orhanobut.logger.Logger;

public class LogUtils {
    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void i(String msg) {
        Logger.i(msg);
    }

    public static void d(String msg) {
        Logger.d(msg);
    }

    public static void e(String msg) {
        Logger.e(msg);
    }

    public static void v(String msg) {
        Logger.v(msg);
    }

    public static void w(String msg) {
        Logger.w(msg);
    }

    public static void json(String msg) {
        Logger.json(msg);
    }

}
