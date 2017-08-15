package com.zwq65.unity.ui.personalcenter;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/08/07
 */

public class PersonalCenterPresenter<V extends PersonalCenterMvpView> extends BasePresenter<V> implements PersonalCenterMvpPresenter<V> {
    @Inject
    public PersonalCenterPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}
