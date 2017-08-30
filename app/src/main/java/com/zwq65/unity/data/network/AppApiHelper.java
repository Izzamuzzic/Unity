package com.zwq65.unity.data.network;

import com.zwq65.unity.data.network.retrofit.RetrofitApiManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * Created by zwq65 on 2017/08/30
 */

public class AppApiHelper implements ApiHelper {
    private RetrofitApiManager retrofitApiManager;

    @Inject
    public AppApiHelper(RetrofitApiManager retrofitApiManager) {
        this.retrofitApiManager = retrofitApiManager;
    }

    @Override
    public Disposable getImagesByPage20(int page, ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return retrofitApiManager.getGankIoApiService().getImagesByPage20(page)
                .compose(RetrofitApiManager.schedulersTransformer()).subscribe(callBack, errorCallBack);
    }

    @Override
    public Disposable getVideosAndIMagesByPage(int page, ApiSubscriberCallBack<List<VideoWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return Flowable.zip(retrofitApiManager.getGankIoApiService().getVideosByPage(page), retrofitApiManager.getGankIoApiService().getImagesByPage(page),
                (restVideoResponse, welfareResponse) -> {
                    List<VideoWithImage> videoWithImageList = new ArrayList<>();
                    if (restVideoResponse != null && restVideoResponse.getData() != null
                            && welfareResponse != null && welfareResponse.getData() != null) {
                        List<Video> videos = restVideoResponse.getData();
                        List<Image> images = welfareResponse.getData();
                        for (int i = 0; i < videos.size(); i++) {
                            if (i < images.size()) {
                                videoWithImageList.add(new VideoWithImage(videos.get(i), images.get(i)));
                            }
                        }
                    }
                    return videoWithImageList;
                }).compose(RetrofitApiManager.schedulersTransformer()).subscribe(callBack, errorCallBack);
    }
}
