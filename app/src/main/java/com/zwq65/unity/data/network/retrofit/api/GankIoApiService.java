package com.zwq65.unity.data.network.retrofit.api;


import com.zwq65.unity.data.network.ApiConstants;
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
     * 获取福利图片
     */
    @GET(ApiConstants.data + ApiConstants.Welfare + ApiConstants.defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Image>>> getImages(@Path("page") int page);

    /**
     * 获取福利图片
     */
    @GET(ApiConstants.random + ApiConstants.Welfare + ApiConstants.defaultPageSize)
    Flowable<GankApiResponse<List<Image>>> getRandomImages();

    /**
     * 获取福利图片（20张）
     */
    @GET(ApiConstants.data + ApiConstants.Welfare + ApiConstants.morePageSize20 + "/{page}")
    Flowable<GankApiResponse<List<Image>>> get20Images(@Path("page") int page);

    /**
     * 获取休息视频
     */
    @GET(ApiConstants.data + ApiConstants.RestVideo + ApiConstants.defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Video>>> getVideos(@Path("page") int page);

    /**
     * 获取android文章
     */
    @GET(ApiConstants.data + ApiConstants.Android + ApiConstants.defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Article>>> getAndroidArticles(@Path("page") int page);

    /**
     * 获取ios文章
     */
    @GET(ApiConstants.data + ApiConstants.IOS + ApiConstants.defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Article>>> getIosArticles(@Path("page") int page);

    /**
     * 获取前端文章
     */
    @GET(ApiConstants.data + ApiConstants.Qianduan + ApiConstants.defaultPageSize + "/{page}")
    Flowable<GankApiResponse<List<Article>>> getQianduanArticles(@Path("page") int page);
}
