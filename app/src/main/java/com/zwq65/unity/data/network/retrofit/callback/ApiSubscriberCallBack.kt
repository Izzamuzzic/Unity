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

package com.zwq65.unity.data.network.retrofit.callback

import com.zwq65.unity.App
import com.zwq65.unity.utils.LogUtils

import io.reactivex.subscribers.DisposableSubscriber

/**
 * ================================================
 * 封装的api接口response回调类
 * <p>
 * Created by NIRVANA on 2017/05/03.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
abstract class ApiSubscriberCallBack<T> : DisposableSubscriber<T>() {

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onComplete() {}

    override fun onError(t: Throwable) {
        LogUtils.e(TAG, t.toString())
        if (t is ApiException) {
            //toast错误信息
            App.getInstance().showToast(t.message)
            onFailure(t.code, t.message)
        }
    }

    /**
     * api接口成功回调
     *
     * @param response T
     */
    abstract fun onSuccess(response: T)

    /**
     * api接口失败回调
     *
     * @param errCode 结果码
     * @param errMsg  错误信息
     */
    abstract fun onFailure(errCode: String, errMsg: String)

    companion object {
        private const val TAG = "ApiSubscriberCallBack"
    }

}
