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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.jingewenku.abrahamcaijin.commonutil.AppFileMgr;
import com.zwq65.unity.R;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/01/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public final class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getImageStorePath() {
        String path;
        if (AppFileMgr.getSdCardIsEnable()) {
            path = AppFileMgr.getSdCardPath();
        } else {
            path = AppFileMgr.getDataAbsolutePath();
        }
        path = path + "Unity" + File.separator + "image" + File.separator;
        return path;
    }

    /**
     * 用于取得recycleView当前最大的position以判断是否许需要加载
     *
     * @param lastPositions recycleView底部的position数组
     * @return 最大的position
     */
    public static int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private static Pattern chinesePattern = Pattern.compile("[\\u4E00-\\u9FA5]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /**
     * 判断content是否含有中文
     *
     * @param content
     */
    public static Boolean verifyChinese(String content) {
        Matcher matcher = chinesePattern.matcher(content);
        return matcher.matches();
    }
    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;

    }
}
