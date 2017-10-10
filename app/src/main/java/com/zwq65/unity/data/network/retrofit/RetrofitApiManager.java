package com.zwq65.unity.data.network.retrofit;

import android.util.Log;

import com.zwq65.unity.utils.LogUtils;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * retrofit网络请求框架
 * Created by zwq65 on 2017/05/03.
 */

public class RetrofitApiManager {
    /**
     * 统一线程处理(compose简化线程)
     *
     * @param <T> 返回数据data实际的 数据
     * @return 返回数据data实际的 数据
     */
    public static <T> FlowableTransformer<T, T> schedulersTransformer() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 自定义拦截器(log request and response data)
     */
    public static class MyInterceptor implements Interceptor {
        @Override
        public Response intercept(@android.support.annotation.NonNull Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            LogUtils.d("request", "request:" + chain.request().url());
            ResponseBody body;
            try {
                body = response.peekBody(1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
            String ss = body.string();
            Log.d("retrofitResponse", ss);
            return response;
        }
    }

}
