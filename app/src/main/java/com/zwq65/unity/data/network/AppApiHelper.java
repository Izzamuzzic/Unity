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

package com.zwq65.unity.data.network;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.zwq65.unity.data.network.retrofit.api.GankIoApiService;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.callback.HttpErrorFuncion;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * ================================================
 * 网络访问实体类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Singleton
public class AppApiHelper implements ApiHelper {
    private GankIoApiService gankIoApiService;

    @Inject
    AppApiHelper(GankIoApiService gankIoApiService) {
        this.gankIoApiService = gankIoApiService;
    }

    /**
     * 获取随机数目的image'list
     *
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void getRandomImages(ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, LifecycleTransformer<GankApiResponse<List<Image>>> lifecycleTransformer) {
        composeScheduler(gankIoApiService.getRandomImages(), callBack, lifecycleTransformer);
    }

    /**
     * 获取page页的image'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void get20Images(int page, ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, LifecycleTransformer<GankApiResponse<List<Image>>> lifecycleTransformer) {
        composeScheduler(gankIoApiService.get20Images(page), callBack, lifecycleTransformer);
    }

    /**
     * 同时获取相同数量的image和video实例
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void getVideosAndImages(int page, ApiSubscriberCallBack<List<Video>> callBack, LifecycleTransformer<List<Video>> lifecycleTransformer) {
        composeScheduler(Flowable.zip(gankIoApiService.getVideos(page), gankIoApiService.getRandomImages(),
                (restVideoResponse, welfareResponse) -> {
                    List<Video> videos = new ArrayList<>();
                    if (restVideoResponse != null && restVideoResponse.getData() != null
                            && welfareResponse != null && welfareResponse.getData() != null) {
                        videos = restVideoResponse.getData();
                        List<Image> images = welfareResponse.getData();
                        for (int i = 0; i < videos.size(); i++) {
                            if (i < images.size()) {
                                videos.get(i).setImage(images.get(i));
                            }
                        }
                    }
                    return videos;
                }),
                callBack,
                lifecycleTransformer);
    }

    /**
     * 获取page页的android'list
     *
     * @param page     页数
     * @param callBack callBack
     */
    @Override
    public void getAndroidArticles(int page,
                                   ApiSubscriberCallBack<List<Article>> callBack,
                                   LifecycleTransformer<List<Article>> lifecycleTransformer) {
        zipArticleWithImage(gankIoApiService.getAndroidArticles(page), callBack, lifecycleTransformer);
    }

    /**
     * 获取page页的ios'list
     *
     * @param page     页数
     * @param callBack callBack
     */
    @Override
    public void getIosArticles(int page,
                               ApiSubscriberCallBack<List<Article>> callBack,
                               LifecycleTransformer<List<Article>> lifecycleTransformer) {
        zipArticleWithImage(gankIoApiService.getIosArticles(page), callBack, lifecycleTransformer);
    }

    /**
     * 获取page页的前端'list
     *
     * @param page     页数
     * @param callBack callBack
     */
    @Override
    public void getQianduanArticles(int page,
                                    ApiSubscriberCallBack<List<Article>> callBack,
                                    LifecycleTransformer<List<Article>> lifecycleTransformer) {
        zipArticleWithImage(gankIoApiService.getQianduanArticles(page), callBack, lifecycleTransformer);
    }

    private void zipArticleWithImage(Flowable<GankApiResponse<List<Article>>> flowable,
                                     ApiSubscriberCallBack<List<Article>> callBack,
                                     LifecycleTransformer<List<Article>> lifecycleTransformer) {
        composeScheduler(Flowable.zip(flowable, gankIoApiService.getRandomImages(),
                (listGankApiResponse, welfareResponse) -> {
                    List<Article> articles = new ArrayList<>();
                    if (listGankApiResponse != null && listGankApiResponse.getData() != null
                            && welfareResponse != null && welfareResponse.getData() != null) {
                        articles = listGankApiResponse.getData();
                        List<Image> images = welfareResponse.getData();
                        for (int i = 0; i < articles.size(); i++) {
                            if (i < images.size()) {
                                articles.get(i).setImage(images.get(i));
                            }
                        }
                    }
                    return articles;
                }), callBack, lifecycleTransformer);
    }

    /**
     * 网络请求统一调度处理
     *
     * @param flowable             api请求Flowable
     * @param callBack             callback回调
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     * @param <T>                  返回类型泛型
     */
    private <T> void composeScheduler(Flowable<T> flowable, ApiSubscriberCallBack<T> callBack, LifecycleTransformer<T> lifecycleTransformer) {
        if (lifecycleTransformer != null) {
            //RxLifeCycle管理流程，防止内存泄漏等异常情况
            flowable = flowable.compose(lifecycleTransformer);
        }
        //简化线程、返回数据处理
        flowable.compose(schedulersTransformer()).subscribe(callBack);
    }

    /**
     * 所有网络请求统一处理(compose简化线程、返回数据处理)
     *
     * @param <T> api返回数据
     * @return api返回数据
     */
    private static <T> FlowableTransformer<T, T> schedulersTransformer() {
        return upstream -> upstream
                //统一切换线程(在io线程进行网络请求；在主线程执行回传ui操作)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //返回数据统一处理(操作失败、异常等情况)
                .onErrorResumeNext(new HttpErrorFuncion<>());
    }
}
