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

package com.zwq65.unity.data.network

import com.trello.rxlifecycle2.LifecycleTransformer
import com.zwq65.unity.data.network.retrofit.api.GankIoApiService
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack
import com.zwq65.unity.data.network.retrofit.callback.HttpErrorFunction
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse
import com.zwq65.unity.data.network.retrofit.response.enity.Article
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.data.network.retrofit.response.enity.Video
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ================================================
 * 网络访问实体类
 *
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Singleton
class AppApiHelper @Inject
internal constructor(private val gankIoApiService: GankIoApiService) : ApiHelper {

    /**
     * 获取随机数目的image'list
     *
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun getRandomImages(callBack: ApiSubscriberCallBack<GankApiResponse<List<Image>>>, lifecycleTransformer: LifecycleTransformer<GankApiResponse<List<Image>>>) {
        composeScheduler(gankIoApiService.randomImages, callBack, lifecycleTransformer)
    }

    /**
     * 获取page页的image'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun get20Images(page: Int, callBack: ApiSubscriberCallBack<GankApiResponse<List<Image>>>, lifecycleTransformer: LifecycleTransformer<GankApiResponse<List<Image>>>) {
        composeScheduler(gankIoApiService.get20Images(page), callBack, lifecycleTransformer)
    }

    /**
     * 同时获取相同数量的image和video实例
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun getVideosAndImages(page: Int, callBack: ApiSubscriberCallBack<List<Video>>, lifecycleTransformer: LifecycleTransformer<List<Video>>) {
        composeScheduler(Flowable.zip(gankIoApiService.getVideos(page), gankIoApiService.randomImages,
                BiFunction<GankApiResponse<List<Video>>, GankApiResponse<List<Image>>, List<Video>> { t1, t2 ->
                    var videos: List<Video> = ArrayList()
                    if (t1.data != null && t2.data != null) {
                        videos = t1.data!!
                        val images = t2.data
                        for (i in videos.indices) {
                            if (i < images!!.size) {
                                videos[i].image = images[i]
                            }
                        }
                    }
                    videos
                }),
                callBack,
                lifecycleTransformer)
    }

    /**
     * 获取page页的android'list
     *
     * @param page     页数
     * @param callBack callBack
     */
    override fun getAndroidArticles(page: Int,
                                    callBack: ApiSubscriberCallBack<List<Article>>,
                                    lifecycleTransformer: LifecycleTransformer<List<Article>>) {
        zipArticleWithImage(gankIoApiService.getAndroidArticles(page), callBack, lifecycleTransformer)
    }

    /**
     * 获取page页的ios'list
     *
     * @param page     页数
     * @param callBack callBack
     */
    override fun getIosArticles(page: Int,
                                callBack: ApiSubscriberCallBack<List<Article>>,
                                lifecycleTransformer: LifecycleTransformer<List<Article>>) {
        zipArticleWithImage(gankIoApiService.getIosArticles(page), callBack, lifecycleTransformer)
    }

    /**
     * 获取page页的前端'list
     *
     * @param page     页数
     * @param callBack callBack
     */
    override fun getQianduanArticles(page: Int,
                                     callBack: ApiSubscriberCallBack<List<Article>>,
                                     lifecycleTransformer: LifecycleTransformer<List<Article>>) {
        zipArticleWithImage(gankIoApiService.getQianduanArticles(page), callBack, lifecycleTransformer)
    }

    private fun zipArticleWithImage(flowable: Flowable<GankApiResponse<List<Article>>>,
                                    callBack: ApiSubscriberCallBack<List<Article>>,
                                    lifecycleTransformer: LifecycleTransformer<List<Article>>) {
        composeScheduler(Flowable.zip(flowable, gankIoApiService.randomImages, BiFunction<GankApiResponse<List<Article>>, GankApiResponse<List<Image>>, List<Article>> { t1, t2 ->
            var articles: List<Article> = ArrayList()
            if (t1.data != null && t2.data != null) {
                articles = t1.data!!
                val images = t2.data
                for (i in articles.indices) {
                    if (i < images!!.size) {
                        articles[i].image = images[i]
                    }
                }
            }
            articles
        }), callBack, lifecycleTransformer)
    }

    /**
     * 网络请求统一调度处理
     *
     * @param flowable             api请求Flowable
     * @param callBack             callback回调
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     * @param <T>                  返回类型泛型
    </T> */
    private fun <T> composeScheduler(flowable: Flowable<T>, callBack: ApiSubscriberCallBack<T>, lifecycleTransformer: LifecycleTransformer<T>?) {
        if (lifecycleTransformer != null) {
            //RxLifeCycle管理流程，防止内存泄漏等异常情况
            flowable.compose(lifecycleTransformer)
        }
        //简化线程、返回数据处理
        flowable.compose(schedulersTransformer()).subscribe(callBack)
    }

    /**
     * 所有网络请求统一处理(compose简化线程、返回数据处理)
     *
     * @param <T> api返回数据
     * @return api返回数据
    </T> */
    private fun <T> schedulersTransformer(): FlowableTransformer<T, T> {

        return FlowableTransformer { upstream ->
            upstream
                    //统一切换线程(在io线程进行网络请求；在主线程执行回传ui操作)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //返回数据统一处理(操作失败、异常等情况)
                    .onErrorResumeNext(HttpErrorFunction())
        }
    }
}
