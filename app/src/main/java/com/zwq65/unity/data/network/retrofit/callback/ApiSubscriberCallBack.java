package com.zwq65.unity.data.network.retrofit.callback;


import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by zwq65 on 2017/07/25
 * 封装的api接口response回调类
 */

public abstract class ApiSubscriberCallBack<T> implements Consumer<T> {

    @Override
    public void accept(@NonNull T t) throws Exception {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);
}
