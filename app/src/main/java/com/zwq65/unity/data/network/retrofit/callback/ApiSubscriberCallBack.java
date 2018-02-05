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

package com.zwq65.unity.data.network.retrofit.callback;

import com.zwq65.unity.utils.LogUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * ================================================
 * 封装的api接口response回调类
 * <p>
 * Created by NIRVANA on 2017/05/03.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public abstract class ApiSubscriberCallBack<T> extends DisposableSubscriber<T> {
    private static final String TAG = "ApiSubscriberCallBack";

    @Override
    public void onNext(T t) {
        LogUtils.e(TAG, "onNext");
        onSuccess(t);
    }

    @Override
    public void onComplete() {
        LogUtils.e(TAG, "onComplete");
    }

    @Override
    public void onError(Throwable t) {
        LogUtils.e(TAG, t.toString());
        //后台返回数据解析异常，常见的是data返回String
        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException) {
            //网络异常（超时、连接、未识别域名...）
            onFailure("999", "网络不给力，请稍后再试");
        } else {
            onFailure("999", "未知异常");
        }
    }

    /**
     * api接口成功回调
     *
     * @param t T
     */
    public abstract void onSuccess(T t);

    /**
     * api接口失败回调
     *
     * @param errCode 结果码
     * @param errMsg  错误信息
     */
    public abstract void onFailure(String errCode, String errMsg);

}
