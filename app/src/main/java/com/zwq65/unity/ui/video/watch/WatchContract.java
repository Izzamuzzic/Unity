package com.zwq65.unity.ui.video.watch;

import com.zwq65.unity.ui._base.BaseContract;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/28
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface WatchContract {
    interface View extends BaseContract.View {

    }

    interface Presenter<V extends BaseContract.View> extends BaseContract.Presenter<V> {

    }
}
