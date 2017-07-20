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

    @Inject
    public AlbumPresenter(CompositeDisposable compositeDisposable) {
        super(compositeDisposable);
    }

    @Override
    public void loadImages(int page) {
        GankIoApiManager.getInstance().getBeautysByPage(page, new ApiSubscriberCallBack<WelfareResponse>() {
            @Override
            public void onSuccess(WelfareResponse welfareResponse) {
                if (welfareResponse != null && welfareResponse.getResults() != null) {
                    getMvpView().loadImages(welfareResponse.getResults());
                }
            }
        });
    }
}
