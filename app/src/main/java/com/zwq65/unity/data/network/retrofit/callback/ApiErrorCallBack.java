package com.zwq65.unity.data.network.retrofit.callback;

import com.zwq65.unity.UnityApp;

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
            UnityApp.showShortToast("网络异常");
        } else {
            UnityApp.showShortToast(t.toString());
        }
        onFailure(t);
    }

    public abstract void onFailure(T t);
}
