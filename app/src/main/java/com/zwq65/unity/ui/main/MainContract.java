package com.zwq65.unity.ui.main;

import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface MainContract {
    interface View extends MvpView {
    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {
        void setNightMode(boolean nightMode);

        Boolean getNightMode();
    }
}
