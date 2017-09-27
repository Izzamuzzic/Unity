package com.zwq65.unity.ui.main;

import com.zwq65.unity.ui._base.BaseContract;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface MainContract {
    interface View extends BaseContract.View {
    }

    interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {
        void setNightMode(boolean nightMode);

        Boolean getNightMode();
    }
}
