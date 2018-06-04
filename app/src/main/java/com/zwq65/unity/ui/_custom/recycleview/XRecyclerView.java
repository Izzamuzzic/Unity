package com.zwq65.unity.ui._custom.recycleview;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class XRecyclerView extends RecyclerView {
    public final String TAG = "XRecyclerView";
    private Context mContext;
    private LoadingListener mLoadingListener;
    private WrapAdapter mWrapAdapter;
    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFootViews = new SparseArray<>();
    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    private YunRefreshHeader mRefreshHeader;
    private boolean isLoadingData;
    public boolean isNoMore;
    private float mLastY = -1;
    private static final float DRAG_RATE = 1.75f;
    // 是否是额外添加FooterView
    private boolean isOther = false;
    /**
     * limit number to call load more
     * 控制多出多少条的时候调用 onLoadMore
     */
    private int limitNumberToCallLoadMore = 2;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        if (pullRefreshEnabled) {
            mRefreshHeader = new YunRefreshHeader(context);
            mHeaderViews.put(0, mRefreshHeader);
        }
        LoadingMoreFooter footView = new LoadingMoreFooter(context);
        addFootView(footView, false);
        mFootViews.get(0).setVisibility(GONE);
    }

    /**
     * 改为公有。供外添加view使用,使用标识
     * 注意：使用后不能使用 上拉加载，否则添加无效
     * 使用时 isOther 传入 true，然后调用 noMoreLoading即可。
     */
    public void addFootView(final View view, boolean isOther) {
        mFootViews.clear();
        mFootViews.put(0, view);
        this.isOther = isOther;
    }

    /**
     * 相当于加一个空白头布局：
     * 只有一个目的：为了滚动条显示在最顶端
     * 因为默认加了刷新头布局，不处理滚动条会下移。
     * 和 setPullRefreshEnabled(false) 一块儿使用
     * 使用下拉头时，此方法不应被使用！
     */
    public void clearHeader() {
        mHeaderViews.clear();
//        final float scale = getContext().getResources().getDisplayMetrics().density;
//        int height = (int) (1.0f * scale + 0.5f);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
//        View view = new View(getContext());
//        view.setLayoutParams(params);
//        mHeaderViews.put(0, view);
    }

    public void addHeaderView(View view) {
        if (pullRefreshEnabled && !(mHeaderViews.get(0) instanceof YunRefreshHeader)) {
            YunRefreshHeader refreshHeader = new YunRefreshHeader(getContext());
            mHeaderViews.put(0, refreshHeader);
            mRefreshHeader = refreshHeader;
        }
        mHeaderViews.put(mHeaderViews.size(), view);
    }

    private void loadMoreComplete() {
        isLoadingData = false;
        View footView = mFootViews.get(0);
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_COMPLETE);
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    public void noMoreLoading() {
        isLoadingData = false;
        final View footView = mFootViews.get(0);
        isNoMore = true;
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
        } else {
            footView.setVisibility(View.GONE);
        }
        // 额外添加的footView
        if (isOther) {
            footView.setVisibility(View.VISIBLE);
        }
    }

    public void refreshComplete() {
        if (isLoadingData) {
            loadMoreComplete();
        } else {
            loadMoreComplete();
            if (pullRefreshEnabled) {
                mRefreshHeader.refreshComplete();
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    /**
     * 使用{@link #onScrollStateChanged(int)}监听{@link RecyclerView}滑动底部监听有延迟bug；
     * 改为{@link #onScrollChanged(int, int, int, int)}实现监听
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!canScrollVertically(1) && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
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
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - limitNumberToCallLoadMore
                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isNoMore
                    && mRefreshHeader.getState() < YunRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true;
                View footView = mFootViews.get(0);
                if (footView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    footView.setVisibility(View.VISIBLE);
                }
                mLoadingListener.onLoadMore();
                //加载数据，recycleView停止滑动
                stopScroll();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < YunRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                // reset
                mLastY = -1;
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                            isNoMore = false;
                            final View footView = mFootViews.get(0);
                            if (footView instanceof LoadingMoreFooter) {
                                if (footView.getVisibility() != View.GONE) {
                                    footView.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    public boolean isOnTop() {
        if (mHeaderViews == null || mHeaderViews.size() == 0) {
            return false;
        }

        View view = mHeaderViews.get(0);
        return view.getParent() != null;
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };


    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        this.pullRefreshEnabled = pullRefreshEnabled;
    }

    public void setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        this.loadingMoreEnabled = loadingMoreEnabled;
        if (!loadingMoreEnabled) {
            if (mFootViews != null) {
                mFootViews.remove(0);
            }
        } else {
            if (mFootViews != null) {
                LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
                addFootView(footView, false);
            }
        }
    }

    public void setLoadMoreGone() {
        if (mFootViews == null) {
            return;
        }
        View footView = mFootViews.get(0);
        if (footView != null && footView instanceof LoadingMoreFooter) {
            mFootViews.remove(0);
        }
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    public void reset() {
        isNoMore = false;
        final View footView = mFootViews.get(0);
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).reSet();
        }
    }
}
