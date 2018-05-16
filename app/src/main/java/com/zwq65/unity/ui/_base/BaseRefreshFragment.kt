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

package com.zwq65.unity.ui._base

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.ViewStub
import android.widget.TextView
import com.zwq65.unity.R
import com.zwq65.unity.ui._custom.recycleview.XRecyclerView
import com.zwq65.unity.utils.bind

/**
 * ================================================
 * some view that has handle of refresh should extends [BaseRefreshFragment]
 *
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
abstract class BaseRefreshFragment<T, V : RefreshMvpView<T>, P : BaseContract.Presenter<V>> : BaseFragment<V, P>(), RefreshMvpView<T> {

    lateinit var mRecyclerView: XRecyclerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mVsNoNetwork: ViewStub
    /**
     * ViewStub加載的view
     */
    private var mInflateView: View? = null
    /**
     * 网络出错view的TextView
     */
    private var mTvErrorMessage: TextView? = null
    private var isRefreshing: Boolean = false
    var isLoading: Boolean = false

    /**
     * 获取RecycleView的spanCount
     *
     * @return If orientation is vertical, spanCount is number of columns. If orientation is horizontal, spanCount is number of rows.
     */
    abstract val spanCount: Int

    override fun initView() {
        //init views
        mRecyclerView = bind(rootView, R.id.recyclerView)
        mSwipeRefreshLayout = bind(rootView, R.id.swipeRefreshLayout)
        mVsNoNetwork = bind(rootView, R.id.vs_no_network)

        if (spanCount == 1) {
            mRecyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            mRecyclerView.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        }

        mRecyclerView.setPullRefreshEnabled(false)
        mRecyclerView.clearHeader()
        //item加载动画（默认）
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        (mRecyclerView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
        //下拉加載監聽
        mRecyclerView.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {

            }

            override fun onLoadMore() {
                if (isLoading) {
                    return
                }
                isLoading = true
                requestDataLoad()
            }
        })
        //上拉刷新監聽
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        mSwipeRefreshLayout.setOnRefreshListener {
            if (isRefreshing) {
                return@setOnRefreshListener
            }
            isRefreshing = true
            requestDataRefresh()
        }
    }

    override fun initData(saveInstanceState: Bundle?) {
        //第一次加载页面开始刷新动画
        setRefresh(true)
    }

    override fun onToolbarClick() {
        mRecyclerView.smoothScrollToPosition(0)
    }

    /**
     * 刷新数据(子类复写该方法)
     */
    abstract fun requestDataRefresh()

    /**
     * 加载数据(子类复写该方法)
     */
    abstract fun requestDataLoad()

    fun setRefresh(refresh: Boolean) {
        if (refresh == isRefreshing) {
            return
        }
        if (refresh) {
            isRefreshing = true
            mSwipeRefreshLayout.isRefreshing = true
        } else {
            isRefreshing = false
            //防止刷新太快，让刷新动画保留一会儿~
            mSwipeRefreshLayout.postDelayed({
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }, 500)
        }
    }

    override fun refreshData(list: List<T>) {
        setRefresh(false)
        mRecyclerView.refreshComplete()
        //如果加载失败view显示过的话，隐藏之
        mInflateView?.visibility = View.GONE
    }

    override fun loadData(list: List<T>) {
        isLoading = false
        mRecyclerView.refreshComplete()
    }

    override fun refreshFail(errMsg: String) {
        setRefresh(false)
        mRecyclerView.refreshComplete()
        //加载失败，viewStub加载显示'加载失败‘view
        showErrorView(errMsg)
    }

    override fun loadFail(errMsg: String) {
        isLoading = false
        mRecyclerView.refreshComplete()
    }

    override fun noMoreData() {
        setRefresh(false)
        isLoading = false
        mRecyclerView.noMoreLoading()
    }

    /**
     * 显示加载数据错误页
     *
     * @param errMsg 错误信息
     */
    private fun showErrorView(errMsg: String) {
        if (mInflateView == null) {
            try {
                mInflateView = mVsNoNetwork.inflate()
                mTvErrorMessage = mInflateView?.findViewById(R.id.tv_error_message)
            } catch (e: Exception) {
                e.printStackTrace()
                //viewStub已经执行inflate(),在try catch下setVisibility(VISIBLE)显示即可
                mInflateView?.visibility = View.VISIBLE
            }

        } else {
            mInflateView?.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(errMsg) && mTvErrorMessage != null) {
            mTvErrorMessage?.text = errMsg
        }
    }
}
