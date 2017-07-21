package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.network.retrofit.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.GankIoApiManager;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/07/19.
 */

public class AlbumPresenter<V extends AlbumMvpView> extends BasePresenter<V> implements AlbumMvpPresenter<V> {
    private int page;

    @Inject
    public AlbumPresenter(CompositeDisposable compositeDisposable) {
        super(compositeDisposable);
    }

    @Override
    public void initImages() {
        page = 1;
        loadImages();
    }

    @Override
    public void loadImages() {
        GankIoApiManager.getInstance().getBeautysByPage(page, new ApiSubscriberCallBack<WelfareResponse>() {
            @Override
            public void onSuccess(WelfareResponse welfareResponse) {
                if (welfareResponse != null && welfareResponse.getResults() != null) {
                    page++;
                    getMvpView().loadImages(welfareResponse.getResults());
                } else {
                    getMvpView().noMoreData();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                getMvpView().loadError(t);
            }
        });
    }
}
