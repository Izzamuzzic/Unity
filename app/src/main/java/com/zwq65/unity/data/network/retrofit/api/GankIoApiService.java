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

package com.zwq65.unity.data.network.retrofit.api;


import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * gank io'api
 * Created by zwq65 on 2017/05/03.
 */

public interface GankIoApiService {
    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * 随机数据: http://gank.io/api/random/data/分类/个数
     * <p>
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * e.g. http://gank.io/api/data/福利/10/1
     */
    String GANK_IO_HOST = "http://gank.io/api/";
    String data = "data/";
    String random = "random/data/";

    String Welfare = "福利/";
    String RestVideo = "休息视频/";
    String Android = "Android/";
    String IOS = "iOS/";
    String Qianduan = "前端/";

    String defaultPageSize = "10";
    String morePageSize20 = "20";

    /**
     * 获取福利图片
     */
    @GET(data + Welfare + defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Image>>> getImages(@Path("page") int page);

    /**
     * 获取福利图片
     */
    @GET(random + Welfare + defaultPageSize)
    Flowable<GankApiResponse<List<Image>>> getRandomImages();

    /**
     * 获取福利图片（20张）
     */
    @GET(data + Welfare + morePageSize20 + "/{page}")
    Flowable<GankApiResponse<List<Image>>> get20Images(@Path("page") int page);

    /**
     * 获取休息视频
     */
    @GET(data + RestVideo + defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Video>>> getVideos(@Path("page") int page);

    /**
     * 获取android文章
     */
    @GET(data + Android + defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Article>>> getAndroidArticles(@Path("page") int page);

    /**
     * 获取ios文章
     */
    @GET(data + IOS + defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Article>>> getIosArticles(@Path("page") int page);

    /**
     * 获取前端文章
     */
    @GET(data + Qianduan + defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Article>>> getQianduanArticles(@Path("page") int page);
}
