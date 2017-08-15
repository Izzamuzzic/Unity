package com.zwq65.unity.ui.personalcenter.collectionalbum;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/08/10
 */

public class CollectionAlbumPresenter<V extends CollectionAlbumMvpView> extends BasePresenter<V> implements CollectionAlbumMvpPresenter<V> {
    @Inject
    public CollectionAlbumPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}
