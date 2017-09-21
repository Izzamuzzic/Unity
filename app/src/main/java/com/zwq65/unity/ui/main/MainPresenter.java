package com.zwq65.unity.ui.main;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/09/21
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {
    @Inject
    public MainPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }


    @Override
    public void setNightMode(boolean nightMode) {
        getDataManager().setDayNightmode(nightMode);
    }

    @Override
    public Boolean getNightMode() {
        return getDataManager().getDayNightmode();
    }
}
