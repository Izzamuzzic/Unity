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

import com.zwq65.unity.data.network.retrofit.api.GankIoApiService
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse
import com.zwq65.unity.data.network.retrofit.response.enity.Article
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.data.network.retrofit.response.enity.Video
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
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
     */
    override fun getRandomImages(): Observable<GankApiResponse<List<Image>>> {
        return gankIoApiService.randomImages
    }

    /**
     * 获取page页的image'list
     *
     * @param page                 页数
     */
    override fun get20Images(page: Int): Observable<GankApiResponse<List<Image>>> {
        return gankIoApiService.get20Images(page)
    }

    /**
     * 同时获取相同数量的image和video实例
     *
     * @param page                 页数
     */
    override fun getVideosAndImages(page: Int): Observable<List<Video>> {
        return Observable.zip(gankIoApiService.getVideos(page), gankIoApiService.randomImages,
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
                })
    }

    /**
     * 获取page页的android'list
     *
     * @param page                 页数
     */
    override fun getAndroidArticles(page: Int): Observable<List<Article>> {
        return zipArticleWithImage(gankIoApiService.getAndroidArticles(page))
    }

    /**
     * 获取page页的ios'list
     *
     * @param page                 页数
     */
    override fun getIosArticles(page: Int): Observable<List<Article>> {
        return zipArticleWithImage(gankIoApiService.getIosArticles(page))
    }

    /**
     * 获取page页的前端'list
     *
     * @param page                 页数
     */
    override fun getQianduanArticles(page: Int): Observable<List<Article>> {
        return zipArticleWithImage(gankIoApiService.getQianduanArticles(page))
    }

    private fun zipArticleWithImage(observable: Observable<GankApiResponse<List<Article>>>): Observable<List<Article>> {
        return Observable.zip(observable, gankIoApiService.randomImages, BiFunction<GankApiResponse<List<Article>>, GankApiResponse<List<Image>>, List<Article>> { t1, t2 ->
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
        })
    }
}
