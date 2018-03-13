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


import android.util.Log


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/29
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
object LogUtils {
    fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    fun v(tag: String, msg: String) {
        Log.v(tag, msg)
    }

    fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }

    fun i(msg: String) {
        com.blankj.utilcode.util.LogUtils.i(msg)
    }

    fun d(msg: String) {
        com.blankj.utilcode.util.LogUtils.d(msg)
    }

    fun e(msg: String) {
        com.blankj.utilcode.util.LogUtils.e(msg)
    }

    fun v(msg: String) {
        com.blankj.utilcode.util.LogUtils.v(msg)
    }

    fun w(msg: String) {
        com.blankj.utilcode.util.LogUtils.w(msg)
    }

}
