package com.zwq65.unity.ui.setting;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/10/10
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class SettingPresenter<V extends SettingContract.View> extends BasePresenter<V>
        implements SettingContract.Presenter<V> {
    @Inject
    SettingPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }
}
