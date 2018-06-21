package com.zwq65.unity.ui._custom.recycleview


import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View

class XRecyclerView @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(mContext, attrs, defStyle) {
    private var mLoadingListener: LoadingListener? = null
    private var mWrapAdapter: WrapAdapter? = null
    private val mHeaderViews = SparseArray<View>()
    private val mFootViews = SparseArray<View>()
    private var pullRefreshEnabled = true
    private var loadingMoreEnabled = true
    private var mRefreshHeader: YunRefreshHeader? = null
    private var isLoadingData: Boolean = false
    private var isNoMore: Boolean = false
    private var mLastY = -1f
    // 是否是额外添加FooterView
    private var isOther = false
    /**
     * limit number to call load more
     * 控制多出多少条的时候调用 onLoadMore
     */
    private val limitNumberToCallLoadMore = 2

    private val isOnTop: Boolean
        get() {
            if (mHeaderViews.size() == 0) {
                return false
            }

            val view = mHeaderViews.get(0)
            return view.parent != null
        }

    private val mDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            mWrapAdapter!!.notifyDataSetChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mWrapAdapter!!.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mWrapAdapter!!.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            mWrapAdapter!!.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mWrapAdapter!!.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mWrapAdapter!!.notifyItemMoved(fromPosition, toPosition)
        }
    }

    init {
        init(mContext)
    }

    private fun init(context: Context) {
        if (pullRefreshEnabled) {
            mRefreshHeader = YunRefreshHeader(context)
            mHeaderViews.put(0, mRefreshHeader)
        }
        val footView = LoadingMoreFooter(context)
        addFootView(footView, false)
        mFootViews.get(0).visibility = View.GONE
    }

    /**
     * 改为公有。供外添加view使用,使用标识
     * 注意：使用后不能使用 上拉加载，否则添加无效
     * 使用时 isOther 传入 true，然后调用 noMoreLoading即可。
     */
    fun addFootView(view: View, isOther: Boolean) {
        mFootViews.clear()
        mFootViews.put(0, view)
        this.isOther = isOther
    }

    /**
     * 相当于加一个空白头布局：
     * 只有一个目的：为了滚动条显示在最顶端
     * 因为默认加了刷新头布局，不处理滚动条会下移。
     * 和 setPullRefreshEnabled(false) 一块儿使用
     * 使用下拉头时，此方法不应被使用！
     */
    fun clearHeader() {
        mHeaderViews.clear()
        //        final float scale = getContext().getResources().getDisplayMetrics().density;
        //        int height = (int) (1.0f * scale + 0.5f);
        //        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        //        View view = new View(getContext());
        //        view.setLayoutParams(params);
        //        mHeaderViews.put(0, view);
    }

    fun addHeaderView(view: View) {
        if (pullRefreshEnabled && mHeaderViews.get(0) !is YunRefreshHeader) {
            val refreshHeader = YunRefreshHeader(context)
            mHeaderViews.put(0, refreshHeader)
            mRefreshHeader = refreshHeader
        }
        mHeaderViews.put(mHeaderViews.size(), view)
    }

    private fun loadMoreComplete() {
        isLoadingData = false
        val footView = mFootViews.get(0)
        if (footView is LoadingMoreFooter) {
            footView.setState(LoadingMoreFooter.STATE_COMPLETE)
        } else {
            footView.visibility = View.GONE
        }
    }

    fun noMoreLoading() {
        isLoadingData = false
        val footView = mFootViews.get(0)
        isNoMore = true
        if (footView is LoadingMoreFooter) {
            footView.setState(LoadingMoreFooter.STATE_NOMORE)
        } else {
            footView.visibility = View.GONE
        }
        // 额外添加的footView
        if (isOther) {
            footView.visibility = View.VISIBLE
        }
    }

    fun refreshComplete() {
        if (isLoadingData) {
            loadMoreComplete()
        } else {
            loadMoreComplete()
            if (pullRefreshEnabled) {
                mRefreshHeader!!.refreshComplete()
            }
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        mWrapAdapter = WrapAdapter(mHeaderViews, mFootViews, adapter)
        super.setAdapter(mWrapAdapter)
        adapter.registerAdapterDataObserver(mDataObserver)
        mDataObserver.onChanged()
    }

    /**
     * 使用[.onScrollStateChanged]监听[RecyclerView]滑动底部监听有延迟bug；
     * 改为[.onScrollChanged]实现监听
     */
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (!canScrollVertically(1) && !isLoadingData && loadingMoreEnabled) {
            val layoutManager = layoutManager
            val lastVisibleItemPosition: Int
            lastVisibleItemPosition = if (layoutManager is GridLayoutManager) {
                layoutManager.findLastVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val into = IntArray(layoutManager.spanCount)
                layoutManager.findLastVisibleItemPositions(into)
                findMax(into)
            } else {
                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
            if (layoutManager.childCount > 0
                    && lastVisibleItemPosition >= layoutManager.itemCount - limitNumberToCallLoadMore
                    && layoutManager.itemCount > layoutManager.childCount
                    && !isNoMore
                    && mRefreshHeader!!.state < BaseRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true
                val footView = mFootViews.get(0)
                if (footView is LoadingMoreFooter) {
                    footView.setState(LoadingMoreFooter.STATE_LOADING)
                } else {
                    footView.visibility = View.VISIBLE
                }
                mLoadingListener!!.onLoadMore()
                //加载数据，recycleView停止滑动
                stopScroll()
            }
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (mLastY == -1f) {
            mLastY = ev.rawY
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mLastY = ev.rawY
            MotionEvent.ACTION_MOVE -> {
                val deltaY = ev.rawY - mLastY
                mLastY = ev.rawY
                if (isOnTop && pullRefreshEnabled) {
                    mRefreshHeader!!.onMove(deltaY / DRAG_RATE)
                    if (mRefreshHeader!!.visibleHeight > 0 && mRefreshHeader!!.state < BaseRefreshHeader.STATE_REFRESHING) {
                        return false
                    }
                }
            }
            else -> {
                // reset
                mLastY = -1f
                if (isOnTop && pullRefreshEnabled) {
                    if (mRefreshHeader!!.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener!!.onRefresh()
                            isNoMore = false
                            val footView = mFootViews.get(0)
                            if (footView is LoadingMoreFooter) {
                                if (footView.getVisibility() != View.GONE) {
                                    footView.setVisibility(View.GONE)
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }

    private fun findMin(firstPositions: IntArray): Int {
        var min = firstPositions[0]
        for (value in firstPositions) {
            if (value < min) {
                min = value
            }
        }
        return min
    }


    fun setLoadingListener(listener: LoadingListener) {
        mLoadingListener = listener
    }

    fun setPullRefreshEnabled(pullRefreshEnabled: Boolean) {
        this.pullRefreshEnabled = pullRefreshEnabled
    }

    fun setLoadingMoreEnabled(loadingMoreEnabled: Boolean) {
        this.loadingMoreEnabled = loadingMoreEnabled
        if (!loadingMoreEnabled) {
            mFootViews.remove(0)
        } else {
            val footView = LoadingMoreFooter(context)
            addFootView(footView, false)
        }
    }

    fun setLoadMoreGone() {
        val footView = mFootViews.get(0)
        if (footView != null && footView is LoadingMoreFooter) {
            mFootViews.remove(0)
        }
    }

    interface LoadingListener {

        fun onRefresh()

        fun onLoadMore()
    }

    fun reset() {
        isNoMore = false
        val footView = mFootViews.get(0)
        if (footView is LoadingMoreFooter) {
            footView.reSet()
        }
    }

    companion object {
        private const val DRAG_RATE = 1.75f
    }
}
