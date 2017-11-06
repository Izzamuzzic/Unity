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

package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseRefreshFragment;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.album.image.ImageActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * ================================================
 * <p>gank.io开源美图
 * Created by NIRVANA on 2017/08/07
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class AlbumFragment extends BaseRefreshFragment<Image, AlbumContract.View<Image>,
        AlbumContract.Presenter<AlbumContract.View<Image>>> implements AlbumContract.View<Image> {
    @Inject
    AlbumAdapter<Image> mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    public void initView() {
        super.initView();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //item加载动画（默认）
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //item间隔
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter.setOnItemClickListener((image, position) -> gotoContentActivity(position));
        mRecyclerView.setAdapter(mAdapter);
        //添加item拖拽监听帮助类
        ItemTouchHelper helper = new ItemTouchHelper(new MyItemTouchCallBack(mAdapter));
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        super.initData(saveInstanceState);
        initData();
    }

    private void gotoContentActivity(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(ImageActivity.POSITION, position);
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, (ArrayList<Image>) mAdapter.getmDataList());
        mActivity.openActivity(ImageActivity.class, bundle);
    }

    public void initData() {
        mPresenter.init();
    }

    @Override
    public void requestDataRefresh() {
        initData();
    }

    @Override
    public void requestDataLoad() {
        mPresenter.loadImages(false);
    }

    @Override
    public void refreshData(List<Image> list) {
        super.refreshData(list);
        mAdapter.clearItems();
        mAdapter.addItems(list);
    }

    @Override
    public void loadData(List<Image> list) {
        super.loadData(list);
        mAdapter.addItems(list);
    }

}