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
import com.zwq65.unity.R
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.ui._base.BaseRefreshFragment
import com.zwq65.unity.ui.activity.ImageActivity
import com.zwq65.unity.ui.adapter.AlbumAdapter
import com.zwq65.unity.ui.contract.AlbumContract
import java.util.*
import javax.inject.Inject

/**
 * ================================================
 * gank.io开源美图
 * <p>
 * Created by NIRVANA on 2017/08/07
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class AlbumFragment : BaseRefreshFragment<Image, AlbumContract.View<Image>, AlbumContract.Presenter<AlbumContract.View<Image>>>(), AlbumContract.View<Image> {
    @Inject
    lateinit var mAdapter: AlbumAdapter<Image>
    override val layoutId: Int
        get() = R.layout.fragment_album

    /**
     * 获取RecycleView的spanCount
     *
     * @return If orientation is vertical, spanCount is number of columns. If orientation is horizontal, spanCount is number of rows.
     */
    override val spanCount: Int
        get() = 2

    override fun initView() {
        super.initView()
        mAdapter.setOnItemClickListener { _, position -> gotoContentActivity(position) }
        mRecyclerView?.adapter = mAdapter
    }

    override fun initData(saveInstanceState: Bundle?) {
        super.initData(saveInstanceState)
        initData()
    }

    private fun gotoContentActivity(position: Int) {
        val bundle = Bundle()
        bundle.putInt(ImageActivity.POSITION, position)
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, mAdapter.getmDataList() as ArrayList<Image>)
        mActivity?.openActivity(ImageActivity::class.java, bundle)
    }

    fun initData() {
        mPresenter.init()
    }

    override fun requestDataRefresh() {
        initData()
    }

    override fun requestDataLoad() {
        mPresenter.loadImages(false)
    }

    override fun refreshData(list: List<Image>) {
        super.refreshData(list)
        mAdapter.clearItems()
        mAdapter.addItems(list)
    }

    override fun loadData(list: List<Image>) {
        super.loadData(list)
        mAdapter.addItems(list)
    }

}