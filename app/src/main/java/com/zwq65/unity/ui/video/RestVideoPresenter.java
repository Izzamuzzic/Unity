package com.zwq65.unity.ui.video;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.network.retrofit.RetrofitApiManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.VideoWithImage;
import com.zwq65.unity.ui._base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 201/08/15
 */

public class RestVideoPresenter<V extends RestVideoMvpView> extends BasePresenter<V> implements RestVideoMvpPresenter<V> {

    private int page;
    private boolean isLoading;

    @Inject
    public RestVideoPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void init() {
        page = 1;
        isLoading = false;
        loadVideos(true);
    }

    @Override
    public void loadVideos(final Boolean isRefresh) {
        if (isLoading) return;
        isLoading = true;
        getCompositeDisposable().add(RetrofitApiManager.getInstance().getVideosAndIMagesByPage(page, new ApiSubscriberCallBack<List<VideoWithImage>>() {
            @Override
            public void onSuccess(List<VideoWithImage> videoWithImages) {
                if (videoWithImages != null && videoWithImages.size() > 0) {
                    isLoading = false;
                    page++;
                    if (isRefresh) {
                        getMvpView().refreshVideos(videoWithImages);
                    } else {
                        getMvpView().showVideos(videoWithImages);
                    }
                } else {
                    if (videoWithImages != null) {
                        getMvpView().noMoreData();
                    } else {
                        getMvpView().loadFail();
                    }
                }
            }
        }, new ApiErrorCallBack<Throwable>() {
            @Override
            public void onFailure(Throwable t) {
                isLoading = false;
                getMvpView().loadFail();
            }
        }));
    }
}
