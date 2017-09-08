package com.zwq65.unity.ui.test

import com.zwq65.unity.data.DataManager
import com.zwq65.unity.ui._base.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by zwq65 on 2017/09/08
 */

class TestPresenter<V : TestMvpView> @Inject
constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable, override val property: Int) : BasePresenter<V>(dataManager, compositeDisposable), TestMvpPresnter<V> {
    override fun test() {
        super.test()
    }
}
