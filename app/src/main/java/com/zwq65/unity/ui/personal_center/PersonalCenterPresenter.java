package com.zwq65.unity.ui.personal_center;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by zwq65 on 2017/08/07
 */

public class PersonalCenterPresenter<V extends PersonalCenterMvpView> extends BasePresenter<V> implements PersonalCenterMvpPresenter<V> {
    @Inject
    public PersonalCenterPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getCollectionPhoto() {
        getDataManager().getCollectionPictures().subscribe(new Consumer<List<Picture>>() {
            @Override
            public void accept(@NonNull List<Picture> pictures) throws Exception {
                getMvpView().showCollection(pictures);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });

    }

}
