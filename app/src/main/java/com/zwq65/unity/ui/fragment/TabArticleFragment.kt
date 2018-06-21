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
import android.support.annotation.IntDef
import com.zwq65.unity.R
import com.zwq65.unity.data.network.retrofit.response.enity.Article
import com.zwq65.unity.ui._base.BaseRefreshFragment
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter
import com.zwq65.unity.ui.activity.WebArticleActivity
import com.zwq65.unity.ui.adapter.TabArticleAdapter
import com.zwq65.unity.ui.contract.TabArticleContract
import javax.inject.Inject

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class TabArticleFragment : BaseRefreshFragment<Article, TabArticleContract.View<Article>, TabArticleContract.Presenter<TabArticleContract.View<Article>>>(), TabArticleContract.View<Article> {
    @Type
    private var mType: Int = 0
    @Inject
    lateinit var mAdapter: TabArticleAdapter<Article>

    override val layoutId: Int
        get() = R.layout.fragment_tab_article

    /**
     * 获取RecycleView的spanCount
     *
     * @return If orientation is vertical, spanCount is number of columns. If orientation is horizontal, spanCount is number of rows.
     */
    override val spanCount: Int
        get() = 1

    override fun initView() {
        super.initView()
        mAdapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<Article> {
            override fun onClick(t: Article, position: Int) {
                gotoDetailActivity(t)
            }

        })
        mRecyclerView.adapter = mAdapter
    }

    override fun initData(saveInstanceState: Bundle?) {
        super.initData(saveInstanceState)
        getType()
        initData()
    }

    override fun requestDataRefresh() {
        initData()
    }

    override fun requestDataLoad() {
        mPresenter.loadDatas(false)
    }

    fun initData() {
        mPresenter.init()
    }

    private fun gotoDetailActivity(article: Article) {
        val bundle = Bundle()
        bundle.putParcelable(WebArticleActivity.ARTICLE, article)
        mActivity!!.openActivity(WebArticleActivity::class.java, bundle)
    }

    override fun refreshData(list: List<Article>) {
        super.refreshData(list)
        mAdapter.clearItems()
        mAdapter.addItems(list)
    }

    override fun loadData(list: List<Article>) {
        super.loadData(list)
        mAdapter.addItems(list)
    }

    private fun getType() {
        if (arguments != null) {
            mType = arguments!!.getInt(TECH_TAG, ANDROID)
            mPresenter.setType(mType)
        }
    }

    @IntDef(ANDROID, IOS, H5)
    @kotlin.annotation.Retention
    annotation class Type

    companion object {
        const val TECH_TAG = "TECH_TAG"
        const val ANDROID = R.string.android
        const val IOS = R.string.ios
        const val H5 = R.string.qianduan


        fun newInstance(@Type type: Int): TabArticleFragment {
            val args = Bundle()
            args.putInt(TECH_TAG, type)
            val fragment = TabArticleFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
