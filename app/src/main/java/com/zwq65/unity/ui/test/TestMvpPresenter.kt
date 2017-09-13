package com.zwq65.unity.ui.test

import com.zwq65.unity.ui._base.MvpPresenter
import com.zwq65.unity.ui._base.MvpView
import io.reactivex.Observable

/**
 * Created by zwq65 on 2017/09/08
 */
interface TestMvpPresenter<V : MvpView> : MvpPresenter<V> {
    fun test(): Observable<Long>
}