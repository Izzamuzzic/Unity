package com.zwq65.unity.ui.account.tabs.collect;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui._base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/08/14
 */

public class TabCollectionPresenter<V extends TabCollectionContract.View> extends BasePresenter<V> implements TabCollectionContract.Presenter<V> {
    @Inject
    TabCollectionPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public Observable<List<Picture>> getCollectionPictures() {
        return getDataManager().getCollectionPictures();
    }
}
