package com.zwq65.unity.data.network.retrofit;


import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 回调的基类
 */

public abstract class ApiSubscriberCallBack<T> implements Subscriber<T> {
    public Subscription subscription;

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        subscription = s;
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable t) {
        Log.e("onError", t.toString());
        onFailure(t);
    }

    @Override
    public void onComplete() {
    }

    public abstract void onSuccess(T t);

    public void onFailure(Throwable t) {
        if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException) {
            //网络异常（超时、连接、未识别域名...）
        } else {
        }
    }

}
