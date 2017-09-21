package com.zwq65.unity.ui.main;

import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._base.MvpView;

/**
 * Created by zwq65 on 2017/09/21
 */

public interface MainMvpPresenter<V extends MvpView> extends MvpPresenter<V> {
    void setNightMode(boolean nightMode);

    Boolean getNightMode();
}
