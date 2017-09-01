package com.zwq65.unity.data.network;

import com.zwq65.unity.data.network.retrofit.RetrofitApiManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.data.network.retrofit.response.enity.ArticleWithImage;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;

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
        return retrofitApiManager.getGankIoApiService().getRandomImages()
                .compose(RetrofitApiManager.schedulersTransformer()).subscribe(callBack, errorCallBack);
    }

    @Override
    public Disposable get20Images(int page, ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return retrofitApiManager.getGankIoApiService().get20Images(page)
                .compose(RetrofitApiManager.schedulersTransformer()).subscribe(callBack, errorCallBack);
    }

    @Override
    public Disposable getVideosAndIMages(int page, ApiSubscriberCallBack<List<VideoWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return Flowable.zip(retrofitApiManager.getGankIoApiService().getVideos(page), retrofitApiManager.getGankIoApiService().getRandomImages(),
                (restVideoResponse, welfareResponse) -> {
                    List<VideoWithImage> videoWithImageList = new ArrayList<>();
                    if (restVideoResponse != null && restVideoResponse.getData() != null
                            && welfareResponse != null && welfareResponse.getData() != null) {
                        List<Video> videos = restVideoResponse.getData();
                        List<Image> images = welfareResponse.getData();
                        for (int i = 0; i < videos.size(); i++) {
                            if (i < images.size()) {
                                videoWithImageList.add(new VideoWithImage(videos.get(i), images.get(i)));
                            }
                        }
                    }
                    return videoWithImageList;
                }).compose(RetrofitApiManager.schedulersTransformer()).subscribe(callBack, errorCallBack);
    }

    @Override
    public Disposable getAndroidArticles(int page, ApiSubscriberCallBack<List<ArticleWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return zipArticleWithImage(retrofitApiManager.getGankIoApiService().getAndroidArticles(page), callBack, errorCallBack);
    }

    @Override
    public Disposable getIosArticles(int page, ApiSubscriberCallBack<List<ArticleWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return zipArticleWithImage(retrofitApiManager.getGankIoApiService().getIosArticles(page), callBack, errorCallBack);
    }

    @Override
    public Disposable getQianduanArticles(int page, ApiSubscriberCallBack<List<ArticleWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return zipArticleWithImage(retrofitApiManager.getGankIoApiService().getQianduanArticles(page), callBack, errorCallBack);
    }

    private Disposable zipArticleWithImage(Flowable<GankApiResponse<List<Article>>> flowable,
                                           ApiSubscriberCallBack<List<ArticleWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return Flowable.zip(flowable, retrofitApiManager.getGankIoApiService().getRandomImages(),
                (listGankApiResponse, welfareResponse) -> {
                    List<ArticleWithImage> articleWithImageList = new ArrayList<>();
                    if (listGankApiResponse != null && listGankApiResponse.getData() != null
                            && welfareResponse != null && welfareResponse.getData() != null) {
                        List<Article> articles = listGankApiResponse.getData();
                        List<Image> images = welfareResponse.getData();
                        for (int i = 0; i < articles.size(); i++) {
                            if (i < images.size()) {
                                articleWithImageList.add(new ArticleWithImage(articles.get(i), images.get(i)));
                            }
                        }
                    }
                    return articleWithImageList;
                }).compose(RetrofitApiManager.schedulersTransformer()).subscribe(callBack, errorCallBack);
    }

}
