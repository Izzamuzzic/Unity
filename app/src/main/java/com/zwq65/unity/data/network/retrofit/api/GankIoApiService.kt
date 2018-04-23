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

package com.zwq65.unity.data.network.retrofit.api


import com.zwq65.unity.data.network.retrofit.response.GankApiResponse
import com.zwq65.unity.data.network.retrofit.response.enity.Article
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.data.network.retrofit.response.enity.Video

import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * ================================================
 * gank io'apiService
 * <p>
 * Created by NIRVANA on 2017/05/03.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
interface GankIoApiService {

    /**
     * 获取随机福利图片
     *
     * @return List<Image>
    </Image> */
    @get:GET(RANDOM + WELFARE + PAGESIZE_DEFAULT)
    val randomImages: Observable<GankApiResponse<List<Image>>>

    /**
     * 获取福利图片（20张）
     *
     * @param page page
     * @return List<Image>
    </Image> */
    @GET(DATA + WELFARE + PAGESIZE__20 + "/{page}")
    fun get20Images(@Path("page") page: Int): Observable<GankApiResponse<List<Image>>>

    /**
     * 获取休息视频
     *
     * @param page page
     * @return List<Video>
    </Video> */
    @GET(DATA + REST_VIDEO + PAGESIZE_DEFAULT + "/{page}")
    fun getVideos(@Path("page") page: Int): Observable<GankApiResponse<List<Video>>>

    /**
     * 获取android文章
     *
     * @param page page
     * @return List<Article>
    </Article> */
    @GET(DATA + ANDROID + PAGESIZE_DEFAULT + "/{page}")
    fun getAndroidArticles(@Path("page") page: Int): Observable<GankApiResponse<List<Article>>>

    /**
     * 获取ios文章
     *
     * @param page page
     * @return List<Article>
    </Article> */
    @GET(DATA + IOS + PAGESIZE_DEFAULT + "/{page}")
    fun getIosArticles(@Path("page") page: Int): Observable<GankApiResponse<List<Article>>>

    /**
     * 获取前端文章
     *
     * @param page page
     * @return List<Article>
    </Article> */
    @GET(DATA + QIANDUAN + PAGESIZE_DEFAULT + "/{page}")
    fun getQianduanArticles(@Path("page") page: Int): Observable<GankApiResponse<List<Article>>>

    companion object {
        /**
         * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
         * 随机数据: http://gank.io/api/random/data/分类/个数
         *
         * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
         * 请求个数： 数字，大于0
         * 第几页：数字，大于0
         * e.g. http://gank.io/api/data/福利/10/1
         */
        const val GANK_IO_HOST = "http://gank.io/api/"
        const val DATA = "data/"
        const val RANDOM = "random/data/"

        const val WELFARE = "福利/"
        const val REST_VIDEO = "休息视频/"
        const val ANDROID = "Android/"
        const val IOS = "iOS/"
        const val QIANDUAN = "前端/"

        const val PAGESIZE_DEFAULT = "10"
        const val PAGESIZE__20 = "20"
    }
}
