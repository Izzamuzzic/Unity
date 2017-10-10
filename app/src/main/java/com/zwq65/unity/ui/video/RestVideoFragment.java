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

package com.zwq65.unity.ui.video;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.ui._base.BaseRefreshFragment;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.video.watch.WatchActivity;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by zwq65 on 2017/08/15
 */

public class RestVideoFragment extends BaseRefreshFragment<Video, RestVideoContract.View<Video>,
        RestVideoContract.Presenter<RestVideoContract.View<Video>>> implements RestVideoContract.View<Video> {

    @Inject
    RestVideoAdapter<Video> mAdapter;

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
        mAdapter.setOnItemClickListener((video, position) -> gotoWatchActivity(video));
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

    private void gotoWatchActivity(Video video) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WatchActivity.VIDEO, video);
        mActivity.openActivity(WatchActivity.class, bundle);
    }

    @Override
    public void refreshData(List<Video> list) {
        super.refreshData(list);
        mAdapter.clearItems();
        mAdapter.addItems(list);
    }

    @Override
    public void loadData(List<Video> list) {
        super.loadData(list);
        mAdapter.addItems(list);
    }
}
