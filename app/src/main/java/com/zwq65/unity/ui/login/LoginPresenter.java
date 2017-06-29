package com.zwq65.unity.ui.login;

import com.zwq65.unity.ui.base.BasePresenter;
import com.zwq65.unity.ui.base.MvpView;
import com.zwq65.unity.utils.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/06/29.
 */

public class LoginPresenter<V extends MvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    public LoginPresenter(SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
    }

    @Override
    public void login(String account, String password) {

    }

    @Override
    public void register() {

    }

    @Override
    public void forgotPsd() {

    }


    @Override
    public void onDetach() {

    }
}
