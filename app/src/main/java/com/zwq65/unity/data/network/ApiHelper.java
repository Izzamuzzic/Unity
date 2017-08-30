package com.zwq65.unity.data.network;

import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by zwq65 on 2017/08/30
 */

public interface ApiHelper {
    /**
     * 获取page页的image'list
     *
     * @param page          页数
     * @param callBack      callBack
     * @param errorCallBack errorCallBack
     * @return Disposable
     */
    Disposable getImagesByPage20(int page, ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack,
                                 ApiErrorCallBack<Throwable> errorCallBack);

    /**
     * 同时获取相同数量的image和video实例
     *
     * @param page          页数
     * @param callBack      callBack
     * @param errorCallBack errorCallBack
     * @return Disposable
     */
    Disposable getVideosAndIMagesByPage(int page, ApiSubscriberCallBack<List<VideoWithImage>> callBack,
                                        ApiErrorCallBack<Throwable> errorCallBack);
}
