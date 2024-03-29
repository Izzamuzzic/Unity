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

package com.zwq65.unity.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.zwq65.unity.R
import java.io.File


/**
 * ================================================
 *
 * Created by NIRVANA on 2017/01/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
object CommonUtils {

    val imageStorePath: String
        get() {
            var path: String = if (getSdCardIsEnable()) {
                getSdCardPath()
            } else {
                getDataAbsolutePath()
            }
            path = path + "Unity" + File.separator + "image" + File.separator
            return path
        }

    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    /**
     * 判断当前应用是否是debug状态
     */
    fun isApkInDebug(context: Context): Boolean {
        return try {
            val info = context.applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            false
        }

    }

    /**
     * 用于取得recycleView当前最大的position以判断是否许需要加载
     *
     * @param lastPositions recycleView底部的position数组
     * @return 最大的position
     */
    fun findMax(lastPositions: IntArray): Int {
        return lastPositions.max() ?: lastPositions[0]
    }

}
