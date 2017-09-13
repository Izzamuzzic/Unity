package com.zwq65.unity.ui.test;

import android.support.annotation.NonNull;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/09/13
 */

public class TestPresenter<V extends TestMvpView> extends BasePresenter<V> implements TestMvpPresenter<V> {
    @Inject
    public TestPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @NonNull
    @Override
    public Observable<Long> test() {
        return Observable.interval(1, TimeUnit.SECONDS);
    }
}
