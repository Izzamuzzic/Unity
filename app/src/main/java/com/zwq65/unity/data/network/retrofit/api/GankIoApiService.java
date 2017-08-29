package com.zwq65.unity.data.network.retrofit.api;


import com.zwq65.unity.data.network.ApiConstants;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * retrofit_http
 * Created by zwq65 on 2017/05/03.
 */

public interface GankIoApiService {
    /**
     * 获取福利图片（10张）
     */
    @GET(ApiConstants.Welfare + ApiConstants.WelfarePageSize + "/{page}")
    Flowable<GankApiResponse<List<Image>>> getImagesByPage(@Path("page") int page);

    /**
     * 获取福利图片（20张）
     */
    @GET(ApiConstants.Welfare + ApiConstants.WelfarePageSize20 + "/{page}")
    Flowable<GankApiResponse<List<Image>>> getImagesByPage20(@Path("page") int page);

    /**
     * 获取休息视频
     */
    @GET(ApiConstants.RestVideo + ApiConstants.RestVideoPageSize + "/{page}")
    Flowable<GankApiResponse<List<Video>>> getVideosByPage(@Path("page") int page);
}
