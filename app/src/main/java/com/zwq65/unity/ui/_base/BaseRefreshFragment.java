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

package com.zwq65.unity.ui._base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zwq65.unity.R;
import com.zwq65.unity.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/09/14
 */

public abstract class BaseRefreshFragment<T, V extends RefreshMvpView<T>, P extends BaseContract.Presenter<V>> extends BaseFragment<V, P>
        implements RefreshMvpView<T> {
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;

    public boolean isRefreshing;
    public boolean isLoading;

    @Override
    public void initView() {
        //上拉刷新監聽
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (isRefreshing) {
                return;
            }
            isRefreshing = true;
            requestDataRefresh();
        });
        //下拉加載監聽
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                    int lastVisibleItemPosition;
                    if (layoutManager instanceof GridLayoutManager) {
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                        lastVisibleItemPosition = CommonUtils.findMax(into);
                    } else {
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                    if (layoutManager.getChildCount() > 0
                            && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                            && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                        //onLoadMore
                        if (isLoading) {
                            return;
                        }
                        isLoading = true;
                        requestDataLoad();
                    }
                }
            }
        });
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        //第一次加载页面开始刷新动画
        setRefresh(true);
    }

    @Override
    public void onToolbarClick() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    /**
     * 刷新数据(子类复写该方法)
     */
    public abstract void requestDataRefresh();

    /**
     * 加载数据(子类复写该方法)
     */
    public abstract void requestDataLoad();

    public void setRefresh(boolean refresh) {
        if (mSwipeRefreshLayout == null || refresh == isRefreshing) {
            return;
        }
        if (refresh) {
            isRefreshing = true;
            mSwipeRefreshLayout.setRefreshing(true);
        } else {
            isRefreshing = false;
            //防止刷新太快，让刷新动画保留一会儿~
            mSwipeRefreshLayout.postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public void refreshData(List<T> list) {
        setRefresh(false);
    }

    @Override
    public void loadData(List<T> list) {
        setLoading(false);
    }

    @Override
    public void loadFail(Throwable t) {
        setRefresh(false);
        setLoading(false);
    }

    @Override
    public void noMoreData() {
        setRefresh(false);
        setLoading(false);
    }
}
