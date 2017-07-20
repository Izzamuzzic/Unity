package com.zwq65.unity.data.network.retrofit;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zwq65.unity.data.network.Constants;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;

import org.reactivestreams.Publisher;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    public void getBeautysByPage(int page, ApiSubscriberCallBack<WelfareResponse> callBack) {
        getApiService().getBeautysByPage(page).compose(schedulersTransformer())
                .subscribe(callBack);
    }

    private FlowableTransformer schedulersTransformer() {
        return new FlowableTransformer() {
            @Override
            public Publisher apply(Flowable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

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

    private class MyInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
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
