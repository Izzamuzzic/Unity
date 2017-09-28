package com.zwq65.unity.data.network;

import com.zwq65.unity.data.network.retrofit.RetrofitApiManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * Created by zwq65 on 2017/08/30
 */
@Singleton
public class AppApiHelper implements ApiHelper {
    private RetrofitApiManager retrofitApiManager;

    @Inject
    public AppApiHelper(RetrofitApiManager retrofitApiManager) {
        this.retrofitApiManager = retrofitApiManager;
    }

    @Override
    public Disposable getRandomImages(ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return composeSchduler(retrofitApiManager.getGankIoApiService().getRandomImages(), callBack, errorCallBack);
    }

    @Override
    public Disposable get20Images(int page, ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return composeSchduler(retrofitApiManager.getGankIoApiService().get20Images(page), callBack, errorCallBack);
    }

    @Override
    public Disposable getVideosAndIMages(int page, ApiSubscriberCallBack<List<Video>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return composeSchduler(Flowable.zip(retrofitApiManager.getGankIoApiService().getVideos(page), retrofitApiManager.getGankIoApiService().getRandomImages(),
                (restVideoResponse, welfareResponse) -> {
                    List<Video> videos = new ArrayList<>();
                    if (restVideoResponse != null && restVideoResponse.getData() != null
                            && welfareResponse != null && welfareResponse.getData() != null) {
                        videos = restVideoResponse.getData();
                        List<Image> images = welfareResponse.getData();
                        for (int i = 0; i < videos.size(); i++) {
                            if (i < images.size()) {
                                videos.get(i).setImageUrl(images.get(i).getUrl());
                            }
                        }
                        images = null;
                        welfareResponse = null;
                        restVideoResponse = null;
                    }
                    return videos;
                }), callBack, errorCallBack);
    }

    @Override
    public Disposable getAndroidArticles(int page, ApiSubscriberCallBack<List<Article>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return zipArticleWithImage(retrofitApiManager.getGankIoApiService().getAndroidArticles(page), callBack, errorCallBack);
    }

    @Override
    public Disposable getIosArticles(int page, ApiSubscriberCallBack<List<Article>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return zipArticleWithImage(retrofitApiManager.getGankIoApiService().getIosArticles(page), callBack, errorCallBack);
    }

    @Override
    public Disposable getQianduanArticles(int page, ApiSubscriberCallBack<List<Article>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return zipArticleWithImage(retrofitApiManager.getGankIoApiService().getQianduanArticles(page), callBack, errorCallBack);
    }

    private Disposable zipArticleWithImage(Flowable<GankApiResponse<List<Article>>> flowable,
                                           ApiSubscriberCallBack<List<Article>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return composeSchduler(Flowable.zip(flowable, retrofitApiManager.getGankIoApiService().getRandomImages(),
                (listGankApiResponse, welfareResponse) -> {
                    List<Article> articles = new ArrayList<>();
                    if (listGankApiResponse != null && listGankApiResponse.getData() != null
                            && welfareResponse != null && welfareResponse.getData() != null) {
                        articles = listGankApiResponse.getData();
                        List<Image> images = welfareResponse.getData();
                        for (int i = 0; i < articles.size(); i++) {
                            if (i < images.size()) {
                                articles.get(i).setImageUrl(images.get(i).getUrl());
                            }
                        }
                        images = null;
                        welfareResponse = null;
                        listGankApiResponse = null;
                    }
                    return articles;
                }), callBack, errorCallBack);
    }

    /**
     * 统一线程调度
     *
     * @param flowable      api请求Flowable
     * @param callBack      success回调
     * @param errorCallBack error回调
     * @param <T>           返回类型泛型
     * @return Disposable
     */
    private <T> Disposable composeSchduler(Flowable<T> flowable, ApiSubscriberCallBack<T> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return flowable.compose(RetrofitApiManager.schedulersTransformer()).subscribe(callBack, errorCallBack);
    }
}
