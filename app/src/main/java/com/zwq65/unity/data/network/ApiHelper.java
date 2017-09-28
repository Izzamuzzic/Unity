package com.zwq65.unity.data.network;

import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by zwq65 on 2017/08/30
 */

public interface ApiHelper {
    /**
     * 获取随机数目的image'list
     *
     * @param callBack      callBack
     * @param errorCallBack errorCallBack
     * @return Disposable
     */
    Disposable getRandomImages(ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack,
                               ApiErrorCallBack<Throwable> errorCallBack);

    /**
     * 获取page页的image'list
     *
     * @param page          页数
     * @param callBack      callBack
     * @param errorCallBack errorCallBack
     * @return Disposable
     */
    Disposable get20Images(int page, ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack,
                           ApiErrorCallBack<Throwable> errorCallBack);

    /**
     * 同时获取相同数量的image和video实例
     *
     * @param page          页数
     * @param callBack      callBack
     * @param errorCallBack errorCallBack
     * @return Disposable
     */
    Disposable getVideosAndIMages(int page, ApiSubscriberCallBack<List<Video>> callBack,
                                  ApiErrorCallBack<Throwable> errorCallBack);

    /**
     * 获取page页的android'list
     *
     * @param page          页数
     * @param callBack      callBack
     * @param errorCallBack errorCallBack
     * @return Disposable
     */
    Disposable getAndroidArticles(int page, ApiSubscriberCallBack<List<Article>> callBack,
                                  ApiErrorCallBack<Throwable> errorCallBack);

    /**
     * 获取page页的ios'list
     *
     * @param page          页数
     * @param callBack      callBack
     * @param errorCallBack errorCallBack
     * @return Disposable
     */
    Disposable getIosArticles(int page, ApiSubscriberCallBack<List<Article>> callBack,
                              ApiErrorCallBack<Throwable> errorCallBack);

    /**
     * 获取page页的前端'list
     *
     * @param page          页数
     * @param callBack      callBack
     * @param errorCallBack errorCallBack
     * @return Disposable
     */
    Disposable getQianduanArticles(int page, ApiSubscriberCallBack<List<Article>> callBack,
                                   ApiErrorCallBack<Throwable> errorCallBack);

}
