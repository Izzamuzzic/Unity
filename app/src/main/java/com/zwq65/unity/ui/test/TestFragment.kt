package com.zwq65.unity.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseFragment

/**
 * Created by zwq65 on 2017/09/08
 */
class TestFragment : BaseFragment() {
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {

        val view = inflater.inflate(R.layout.fragment_test, container, false)
        setUnBinder { ButterKnife.bind(this, view) }
        return view
    }

    override fun initData(saveInstanceState: Bundle) {
    }

}