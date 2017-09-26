package com.zwq65.unity.ui.video;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;
import com.zwq65.unity.ui._base.BaseRefreshFragment;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.video.watch.WatchActivity;

import java.util.List;


/**
 * Created by zwq65 on 2017/08/15
 */

public class RestVideoFragment<V extends RestVideoContract.View<VideoWithImage>> extends BaseRefreshFragment<VideoWithImage, V,
        RestVideoContract.Presenter<V>> implements RestVideoContract.View<VideoWithImage> {

    RestVideoAdapter mAdapter;

    @Override
    public void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rest_video;
    }

    @Override
    public void initView() {
        super.initView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        mRecyclerView.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new RestVideoAdapter();
        mAdapter.setOnItemClickListener((videoWithImage, position) -> gotoWatchActivity(videoWithImage));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        super.initData(saveInstanceState);
        initData();
    }

    @Override
    public void requestDataRefresh() {
        initData();
    }

    @Override
    public void requestDataLoad() {
        mPresenter.loadVideos(false);

    }

    public void initData() {
        mPresenter.init();
    }

    private void gotoWatchActivity(VideoWithImage videoWithImage) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WatchActivity.VIDEO_WITH_IMAGE, videoWithImage);
        mActivity.openActivity(WatchActivity.class, bundle);
    }

    @Override
    public void refreshData(List<VideoWithImage> list) {
        super.refreshData(list);
        mAdapter.clearItems();
        mAdapter.addItems(list);
    }

    @Override
    public void loadData(List<VideoWithImage> list) {
        super.loadData(list);
        mAdapter.addItems(list);
    }
}
