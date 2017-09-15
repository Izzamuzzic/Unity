package com.zwq65.unity.ui.video;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;
import com.zwq65.unity.ui._base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 201/08/15
 */

public class RestVideoPresenter<V extends RestVideoMvpView<VideoWithImage>> extends BasePresenter<V> implements RestVideoMvpPresenter<V> {
    private int page;

    @Inject
    public RestVideoPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void init() {
        page = 1;
        loadVideos(true);
    }

    @Override
    public void loadVideos(final Boolean isRefresh) {
        getCompositeDisposable().add(
                getDataManager().getVideosAndIMages(page, new ApiSubscriberCallBack<List<VideoWithImage>>() {
                    @Override
                    public void onSuccess(List<VideoWithImage> videoWithImages) {
                        if (videoWithImages != null && videoWithImages.size() > 0) {
                            page++;
                            if (isRefresh) {
                                getMvpView().refreshData(videoWithImages);
                            } else {
                                getMvpView().loadData(videoWithImages);
                            }
                        } else {
                            getMvpView().noMoreData();
                        }
                    }
                }, new ApiErrorCallBack<Throwable>() {
                    @Override
                    public void onFailure(Throwable t) {
                        getMvpView().loadFail(t);
                    }
                }));
    }
}
