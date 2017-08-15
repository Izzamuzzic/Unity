package com.zwq65.unity.ui.video;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.phoenix.PullToRefreshView;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.RestVideoResponse;
import com.zwq65.unity.ui._base.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_rest_video, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        mPresenter.loadVideos(true);
    }

    @Override
    public void showVideos(List<RestVideoResponse.Video> videoList) {

    }
}
