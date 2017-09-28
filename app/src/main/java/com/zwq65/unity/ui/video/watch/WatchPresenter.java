package com.zwq65.unity.ui.video.watch;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/28
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class WatchPresenter<V extends WatchContract.View> extends BasePresenter<V>
        implements WatchContract.Presenter<V> {
    @Inject
    WatchPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }
}
