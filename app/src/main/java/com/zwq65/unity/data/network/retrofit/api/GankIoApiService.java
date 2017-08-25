package com.zwq65.unity.data.network.retrofit.api;


import com.zwq65.unity.data.network.ApiConstants;
import com.zwq65.unity.data.network.retrofit.response.RestVideoResponse;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;

import io.reactivex.Observable;
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
    Observable<WelfareResponse> getImagesByPage(@Path("page") int page);

    /**
     * 获取福利图片（20张）
     */
    @GET(ApiConstants.Welfare + ApiConstants.WelfarePageSize20 + "/{page}")
    Observable<WelfareResponse> getImagesByPage20(@Path("page") int page);

    /**
     * 获取休息视频
     */
    @GET(ApiConstants.RestVideo + ApiConstants.RestVideoPageSize + "/{page}")
    Observable<RestVideoResponse> getVideosByPage(@Path("page") int page);
}
