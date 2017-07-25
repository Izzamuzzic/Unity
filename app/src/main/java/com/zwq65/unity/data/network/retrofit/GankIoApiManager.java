package com.zwq65.unity.data.network.retrofit;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zwq65.unity.data.network.Constants;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.utils.LogUtils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit网络请求框架
 * Created by zwq65 on 2017/05/03.
 */

public class GankIoApiManager {
    private GankIoApiService apiService;
    private static GankIoApiManager apiManager;

    public synchronized static GankIoApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new GankIoApiManager();
        }
        return apiManager;
    }

    public Disposable getImagesByPage(int page, ApiSubscriberCallBack<WelfareResponse> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return getApiService().getImagesByPage(page).compose(schedulersTransformer()).subscribe(callBack, errorCallBack);
    }

    /**
     * @return retrofit_http_api
     */
    private GankIoApiService getApiService() {
        if (apiService == null) {
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .addInterceptor(new MyInterceptor())
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.GANK_IO_HOST)
                    .client(okClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(GankIoApiService.class);
        }
        return apiService;
    }

    /**
     * @return 包装Observable，使之采用统一的线程调度
     */
    private ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(@NonNull Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 自定义拦截器(log request and response data)
     */
    private class MyInterceptor implements Interceptor {
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
