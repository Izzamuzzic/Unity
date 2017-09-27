package com.zwq65.unity.ui.test

import com.zwq65.unity.ui._base.BaseContract

/**
 *================================================
 *
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
public interface TestContract {

    interface View :BaseContract.View

    interface Presenter<V : BaseContract.View> : BaseContract.Presenter<V> {
        fun test()
    }
}