package com.zwq65.unity.ui.setting;

import com.zwq65.unity.ui._base.BaseContract;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/10/10
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface SettingContract {
    interface View extends BaseContract.View {

    }

    interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {

    }
}
