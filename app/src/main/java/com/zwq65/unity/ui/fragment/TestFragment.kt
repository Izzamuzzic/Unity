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
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.zwq65.unity.R
import com.zwq65.unity.data.db.model.GroupInfo
import com.zwq65.unity.ui._base.BaseFragment
import com.zwq65.unity.ui._custom.recycleview.HeadItemDecoration
import com.zwq65.unity.ui.adapter.TestAdapter
import com.zwq65.unity.ui.contract.TestContract
import kotlinx.android.synthetic.main.fragment_test.*
import javax.inject.Inject


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/13.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class TestFragment : BaseFragment<TestContract.View, TestContract.Presenter<TestContract.View>>(), TestContract.View {

    @Inject
    lateinit var mAdapter: TestAdapter<Int>
    override val layoutId: Int
        get() = R.layout.fragment_test

    override fun initView() {
        mRecyclerView?.layoutManager = LinearLayoutManager(context)
        mRecyclerView?.itemAnimator = DefaultItemAnimator()
        var mHeadItemDecoration = HeadItemDecoration()
        mHeadItemDecoration.mGroupInfoCallback = object : HeadItemDecoration.GroupInfoCallback {
            override fun getGroupInfo(position: Int): GroupInfo {
                /**
                 * 分组逻辑，这里为了测试每5个数据为一组。大家可以在实际开发中
                 * 替换为真正的需求逻辑
                 */
                val groupId = position / 5
                val index = position % 5
                return GroupInfo(groupId, index, groupId.toString())
            }
        }
        mRecyclerView?.addItemDecoration(mHeadItemDecoration)
        mRecyclerView?.adapter = mAdapter
        for (i in 1..30) {
            mAdapter.addItem(i)
        }
    }

    override fun initData(saveInstanceState: Bundle?) {

    }


}
