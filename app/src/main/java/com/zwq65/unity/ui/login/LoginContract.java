package com.zwq65.unity.ui.login;

import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface LoginContract {
    interface View extends MvpView {
        void openMainActivity();

        void openRegisterActivity();
    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {
        void login(String account, String password);

        void register();

        void forgotPsd();
    }
}
