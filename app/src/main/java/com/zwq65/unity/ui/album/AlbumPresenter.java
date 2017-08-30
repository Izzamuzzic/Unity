package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BasePresenter;

import java.util.List;

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
    public void init() {
        page = 1;
        isLoading = false;
        loadImages(true);
    }

    @Override
    public void loadImages(final Boolean isRefresh) {
        if (isLoading) return;
        isLoading = true;
        getCompositeDisposable().add(
                getDataManager().get20Images(page, new ApiSubscriberCallBack<GankApiResponse<List<Image>>>() {
                    @Override
                    public void onSuccess(GankApiResponse<List<Image>> welfareResponse) {
                        if (welfareResponse != null && welfareResponse.getData() != null) {
                            page++;
                            if (isRefresh) {
                                getMvpView().refreshImages(welfareResponse.getData());
                            } else {
                                getMvpView().showImages(welfareResponse.getData());
                            }
                        } else {
                            getMvpView().noMoreData();
                        }
                        isLoading = false;
                    }
                }, new ApiErrorCallBack<Throwable>() {
                    @Override
                    public void onFailure(Throwable t) {
                        isLoading = false;
                    }
                })
        );
    }
}
