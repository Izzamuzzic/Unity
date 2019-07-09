/*
 *    Copyright [2017] [NIRVANA PRIVATE LIMITED]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zwq65.unity.ui.fragment

import android.os.Bundle
import android.widget.LinearLayout
import com.blankj.utilcode.util.LogUtils
import com.zwq65.unity.R
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.ui._base.BaseFragment
import com.zwq65.unity.ui._custom.layout.OnBounceDistanceChangeListener
import com.zwq65.unity.ui.contract.TestContract
import kotlinx.android.synthetic.main.fragment_test.*


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/13.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class TestFragment : BaseFragment<TestContract.View, TestContract.Presenter<TestContract.View>>(), TestContract.View {

    override val layoutId: Int
        get() = R.layout.fragment_test

    override fun initView() {
    }

    private var mHeight: Int? = null

    override fun initData(saveInstanceState: Bundle?) {
        mView?.let {
            it.post {
                mHeight = it.height
            }
        }
        mReboundLayout?.mOnBounceDistanceChangeListener = object : OnBounceDistanceChangeListener {
            override fun onDistanceChange(distance: Int, direction: Int) {
                LogUtils.i("onDistanceChange distance: $distance")
                mView?.let {
                    it.post {
                        it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                mHeight!! + distance)
                    }
                }
            }

            override fun onFingerUp(distance: Int, direction: Int) {
                LogUtils.i("onFingerUp distance: $distance")
                mView?.let {
                    it.post {
                        mView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mHeight
                                ?: 0)
                    }
                }
            }

        }
    }

    /**
     * 加载数据
     *
     * @param list 数据列表
     */
    override fun loadData(list: List<Image>) {
    }
}
