package com.zwq65.unity.ui.account;

import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/08/07
 */

public class AccountPresenter<V extends AccountContract.View> extends BasePresenter<V> implements AccountContract.Presenter<V> {
    @Inject
    AccountPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}
