package com.zwq65.unity.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.Unbinder
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseFragment
import javax.inject.Inject

/**
 * Created by zwq65 on 2017/09/12
 */

class TestFragment : BaseFragment<TestMvpView, TestMvpPresenter<TestMvpView>>(), TestMvpView {
    @Inject
    internal var mPresnter: TestMvpPresenter<TestMvpView>? = null

    override fun setmPresenter(): TestMvpPresenter<TestMvpView>? {
        activityComponent.inject(this)
        mPresnter?.onAttach(this)
        return mPresnter
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.fragment_test, container, false)

    override fun setUnBinder(view: View): Unbinder? = null

    override fun initView() {

    }

    override fun initData(saveInstanceState: Bundle) {

    }
}
