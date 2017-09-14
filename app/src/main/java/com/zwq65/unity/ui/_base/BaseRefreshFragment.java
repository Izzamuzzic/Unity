package com.zwq65.unity.ui._base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zwq65.unity.R;

import java.util.List;

import butterknife.BindView;

import static com.zwq65.unity.ui._custom.recycleview.XRecyclerView.findMax;

/**
 * Created by zwq65 on 2017/09/14
 */

public abstract class BaseRefreshFragment<T> extends BaseFragment implements RefreshMvpView<T> {
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    public RecyclerView mRecyclerView;

    public boolean isRefreshing;
    public boolean isLoading;

    @Override
    public void initView() {
        //上拉刷新監聽
        mSwipeRefreshLayout.setOnRefreshListener(this::requestDataRefresh);
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
                        lastVisibleItemPosition = findMax(into);
                    } else {
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                    if (layoutManager.getChildCount() > 0
                            && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                            && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                        //onLoadMore
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

    /**
     * 刷新数据(子类复写该方法)
     */
    public void requestDataRefresh() {
        isRefreshing = true;
    }

    /**
     * 加载数据(子类复写该方法)
     */
    public void requestDataLoad() {
        if (isLoading) {
            return;
        }
        isLoading = true;
    }

    public void setRefresh(boolean refresh) {
        if (mSwipeRefreshLayout == null || refresh == isRefreshing) {
            return;
        }
        if (refresh) {
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

    public void setRequestFinish() {
        isRefreshing = false;
        isLoading = false;
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
        setRequestFinish();
    }

    @Override
    public void noMoreData() {
        setRequestFinish();
    }
}