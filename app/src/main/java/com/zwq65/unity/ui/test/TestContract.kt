package com.zwq65.unity.ui.test

import com.zwq65.unity.ui._base.MvpPresenter
import com.zwq65.unity.ui._base.MvpView

/**
 *================================================
 *
 * Created by NIRVANA on 2017/09/26
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
public interface TestContract {

    interface View : MvpView

    interface Presenter<V : MvpView> : MvpPresenter<V> {
        fun test()
    }
}