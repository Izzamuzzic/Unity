package com.zwq65.unity.data.network.retrofit;


import com.zwq65.unity.data.network.Constants;
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
     * 获取福利图片(pagesize=10)
     */
    @GET(Constants.welfare10 + "/{page}")
    Observable<WelfareResponse> getImagesByPage(@Path("page") int page);

    /**
     * 获取福利图片(pagesize=1)
     */
    @GET(Constants.welfare10 + "/{page}")
    Observable<WelfareResponse> getImageByPage(@Path("page") int page);

}
