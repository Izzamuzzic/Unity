package com.zwq65.unity.ui.account;

import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface AccountContract {
    interface View extends MvpView {

    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {

    }
}
