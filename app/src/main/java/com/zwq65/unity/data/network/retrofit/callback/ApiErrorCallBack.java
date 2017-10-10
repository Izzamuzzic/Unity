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

import com.zwq65.unity.App;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;
import io.reactivex.annotations.NonNull;

/**
 * Created by zwq65 on 2017/07/25
 * 封装的api接口error回调类
 */

public abstract class ApiErrorCallBack<T extends Throwable> implements Consumer<T> {

    @Override
    public void accept(@NonNull T t) throws Exception {
        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException) {
            //网络异常（超时、连接、未识别域名...）
            App.showShortToast("网络异常");
        } else {
            App.showShortToast(t.toString());
        }
        onFailure(t);
    }

    public abstract void onFailure(T t);
}
