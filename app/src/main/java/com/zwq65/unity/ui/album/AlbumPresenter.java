package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.network.retrofit.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.GankIoApiManager;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/07/19
 */

public class AlbumPresenter<V extends AlbumMvpView> extends BasePresenter<V> implements AlbumMvpPresenter<V> {
    private int page;
    private boolean isLoading;

    @Inject
    AlbumPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void initImages() {
        page = 1;
        loadImages(true);
    }

    @Override
    public void loadImages(final Boolean isRefresh) {
        if (isLoading) return;
        isLoading = true;
        getCompositeDisposable().add(
                GankIoApiManager.getInstance().getImagesByPage(page, new ApiSubscriberCallBack<WelfareResponse>() {
                    @Override
                    public void onSuccess(WelfareResponse welfareResponse) {
                        if (welfareResponse != null && welfareResponse.getResults() != null) {
                            page++;
                            if (isRefresh) {
                                getMvpView().refreshImages(welfareResponse.getResults());
                            } else {
                                getMvpView().loadImages(welfareResponse.getResults());
                            }
                        } else {
                            getMvpView().noMoreData();
                        }
                        isLoading = false;
                    }
                }, new ApiErrorCallBack<Throwable>() {
                    @Override
                    public void onFailure(Throwable t) {
                        super.onFailure(t);
                        getMvpView().loadError(t);
                        isLoading = false;
                    }
                })
        );
    }
}
