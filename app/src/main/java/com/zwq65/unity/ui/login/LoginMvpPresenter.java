package com.zwq65.unity.ui.login;

import com.zwq65.unity.di.PerActivity;
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

/**
 * Created by zwq65 on 2017/06/29.
 */

@PerActivity
public interface LoginMvpPresenter<V extends MvpView> extends MvpPresenter<V> {
    void login(String account, String password);

    void register();

    void forgotPsd();
}
