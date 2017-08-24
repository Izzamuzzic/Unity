package com.zwq65.unity.ui.video;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.phoenix.PullToRefreshView;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.VideoWithImage;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._base.adapter.BaseRecyclerViewAdapter;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.video.watch.WatchActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zwq65.unity.ui.custom.recycleview.XRecyclerView.findMax;

/**
 * Created by zwq65 on 2017/08/15
 */

public class RestVideoFragment extends BaseFragment implements RestVideoMvpView {

    @Inject
    RestVideoMvpPresenter<RestVideoMvpView> mPresenter;
    @BindView(R.id.rv_videos)
    RecyclerView rvVideos;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefresh;

    RestVideoAdapter mAdapter;

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_rest_video, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        setmPresenter(mPresenter);
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        initView();
        initData();
    }

    private void initView() {
        rvVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVideos.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rvVideos.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) rvVideos.getItemAnimator()).setSupportsChangeAnimations(false);
        //上拉刷新監聽
        pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        //下拉加載監聽
        rvVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = rvVideos.getLayoutManager();
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
                        mPresenter.loadVideos(false);
                    }
                }
            }
        });
        mAdapter = new RestVideoAdapter(getContext());
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<VideoWithImage>() {
            @Override
            public void onClick(VideoWithImage videoWithImage, int position) {
                gotoWatchActivity(videoWithImage);
            }
        });
        rvVideos.setAdapter(mAdapter);
    }

    private void gotoWatchActivity(VideoWithImage videoWithImage) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WatchActivity.VIDEO_WITH_IMAGE, videoWithImage);
        mActivity.openActivity(WatchActivity.class, bundle);
    }

    public void initData() {
        mPresenter.init();
    }

    @Override
    public void onToolbarClick() {
        rvVideos.smoothScrollToPosition(0);
    }

    @Override
    public void refreshVideos(List<VideoWithImage> videoWithImages) {
        pullToRefresh.setRefreshing(false);
        mAdapter.clearItems();
        mAdapter.addItems(videoWithImages);
    }

    @Override
    public void showVideos(List<VideoWithImage> videoWithImages) {
        mAdapter.addItems(videoWithImages);
    }

    @Override
    public void noMoreData() {
        pullToRefresh.setRefreshing(false);
        showErrorAlert(R.string.no_more_data);
    }

    @Override
    public void loadFail() {
        pullToRefresh.setRefreshing(false);
        showErrorAlert(R.string.load_fail);
    }
}
