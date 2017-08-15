package com.zwq65.unity.data.network.retrofit;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zwq65.unity.data.network.ApiConstants;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.RestVideoResponse;
import com.zwq65.unity.data.network.retrofit.response.RestVideoResponse.Video;
import com.zwq65.unity.data.network.retrofit.response.VideoWithImage;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
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

public class RetrofitApiManager {
    private GankIoApiService gankIoApiService;
    private static volatile RetrofitApiManager apiManager;

    public Disposable getImagesByPage(int page, ApiSubscriberCallBack<WelfareResponse> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return getGankIoApiService().getImagesByPage(page).compose(schedulersTransformer()).subscribe(callBack, errorCallBack);
    }

    public Disposable getVideosByPage(int page, ApiSubscriberCallBack<RestVideoResponse> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return getGankIoApiService().getVideosByPage(page).compose(schedulersTransformer()).subscribe(callBack, errorCallBack);
    }

    public Disposable getVideosAndIMagesByPage(int page, ApiSubscriberCallBack<List<VideoWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return Observable.zip(getGankIoApiService().getVideosByPage(page), getGankIoApiService().getImagesByPage(page),
                new BiFunction<RestVideoResponse, WelfareResponse, List<VideoWithImage>>() {
                    @Override
                    public List<VideoWithImage> apply(@NonNull RestVideoResponse restVideoResponse, @NonNull WelfareResponse welfareResponse) throws Exception {
                        List<VideoWithImage> videoWithImageList = new ArrayList<>();

                        if (restVideoResponse != null && restVideoResponse.getResults() != null
                                && welfareResponse != null && welfareResponse.getResults() != null) {
                            List<Video> videos = restVideoResponse.getResults();
                            List<Image> images = welfareResponse.getResults();
                            for (int i = 0; i < videos.size(); i++) {
                                if (i < images.size()) {
                                    videoWithImageList.add(new VideoWithImage(videos.get(i), images.get(i)));
                                }
                            }
                        }
                        return videoWithImageList;
                    }
                }).compose(schedulersTransformer()).subscribe(callBack, errorCallBack);
    }


    public static RetrofitApiManager getInstance() {
        if (apiManager == null) {
            synchronized (RetrofitApiManager.class) {
                if (apiManager == null) {
                    apiManager = new RetrofitApiManager();
                }
            }
        }
        return apiManager;
    }

    /**
     * @return gank.io'api
     */
    private GankIoApiService getGankIoApiService() {
        if (gankIoApiService == null) {
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .addInterceptor(new MyInterceptor())
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.GANK_IO_HOST)
                    .client(okClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gankIoApiService = retrofit.create(GankIoApiService.class);
        }
        return gankIoApiService;
    }

    /**
     * @return 包装Observable采用统一的线程调度(网络请求在io线程执行, 订阅后切换到Android主线程执行)
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
    private static class MyInterceptor implements Interceptor {
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
