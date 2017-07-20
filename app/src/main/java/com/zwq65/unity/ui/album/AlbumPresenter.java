package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.network.retrofit.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.GankIoApiManager;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/07/19.
 */

public class AlbumPresenter<V extends AlbumMvpView> extends BasePresenter<V> implements AlbumMvpPresenter<V> {

    private int page;
    private List<WelfareResponse.Image> imageList;

    @Inject
    public AlbumPresenter(CompositeDisposable compositeDisposable) {
        super(compositeDisposable);
    }

    @Override
    public void initImages() {
        page = 1;
        imageList = new ArrayList<>();
        loadImages();
    }

    @Override
    public void loadImages() {
        GankIoApiManager.getInstance().getBeautysByPage(page, new ApiSubscriberCallBack<WelfareResponse>() {
            @Override
            public void onSuccess(WelfareResponse welfareResponse) {
                if (welfareResponse != null && welfareResponse.getResults() != null) {
                    imageList.addAll(welfareResponse.getResults());
                    getMvpView().loadImages(imageList);
                    page++;
                }
            }
        });
    }
}
