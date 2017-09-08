package com.zwq65.unity.ui.test;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/09/08
 */

public class TestPresenter<V extends TestMvpView> extends BasePresenter<V> implements TestMvpPresnter<V> {
    public TestPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }
}
