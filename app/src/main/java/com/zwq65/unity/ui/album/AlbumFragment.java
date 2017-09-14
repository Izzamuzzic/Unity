package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseRefreshFragment;
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.album.image.ImageActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zwq65 on 2017/08/07
 * gank.io开源美图
 */

public class AlbumFragment extends BaseRefreshFragment<Image> implements AlbumMvpView<Image> {

    @Inject
    AlbumMvpPresenter<AlbumMvpView<Image>> mPresenter;
    AlbumAdapter<Image> mAdapter;

    @Override
    public MvpPresenter setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
    }

    @Override
    public View inflateLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public Unbinder setUnBinder(View view) {
        return ButterKnife.bind(this, view);
    }

    @Override
    public void initView() {
        super.initView();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        mRecyclerView.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new AlbumAdapter<>(mActivity);
        mAdapter.setOnItemClickListener((image, position) -> gotoContentActivity(position));
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(new MyItemTouchCallBack(mAdapter));//拖拽监听
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

    @Override
    public void onToolbarClick() {
        mRecyclerView.smoothScrollToPosition(0);
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
