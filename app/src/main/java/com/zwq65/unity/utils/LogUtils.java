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

package com.zwq65.unity.utils;


import android.util.Log;

import com.facebook.stetho.common.LogUtil;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/29
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
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
        LogUtil.i(msg);
    }

    public static void d(String msg) {
        LogUtil.d(msg);
    }

    public static void e(String msg) {
        LogUtil.e(msg);
    }

    public static void v(String msg) {
        LogUtil.v(msg);
    }

    public static void w(String msg) {
        LogUtil.w(msg);
    }

}
