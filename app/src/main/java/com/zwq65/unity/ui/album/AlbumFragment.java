package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.phoenix.PullToRefreshView;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.album.image.ImageActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zwq65.unity.ui._custom.recycleview.XRecyclerView.findMax;

/**
 * Created by zwq65 on 2017/08/07
 * gank.io开源美图
 */

public class AlbumFragment extends BaseFragment implements AlbumMvpView {
    @BindView(R.id.rv_albums)
    RecyclerView rvAlbums;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefresh;

    @Inject
    AlbumMvpPresenter<AlbumMvpView> mPresenter;
    AlbumAdapter mAdapter;

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        setmPresenter(mPresenter);
        initView();
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        initData();
    }

    public void initView() {
        rvAlbums.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvAlbums.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rvAlbums.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) rvAlbums.getItemAnimator()).setSupportsChangeAnimations(false);
        //上拉刷新監聽
        pullToRefresh.setOnRefreshListener(this::initData);
        //下拉加載監聽
        rvAlbums.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = rvAlbums.getLayoutManager();
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
                        mPresenter.loadImages(false);
                    }
                }
            }
        });

        mAdapter = new AlbumAdapter(mActivity);
        mAdapter.setOnItemClickListener((image, position) -> gotoContentActivity(position));
        rvAlbums.setAdapter(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(new MyItemTouchCallBack(mAdapter));//拖拽监听
        helper.attachToRecyclerView(rvAlbums);
    }

    private void gotoContentActivity(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(ImageActivity.POSITION, position);
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, (ArrayList<Image>) mAdapter.getmDataList());
        mActivity.openActivity(ImageActivity.class, bundle);
    }

    @Override
    public void onToolbarClick() {
        rvAlbums.smoothScrollToPosition(0);
    }

    public void initData() {
        mPresenter.init();
    }

    @Override
    public void refreshImages(List<Image> imageList) {
        pullToRefresh.setRefreshing(false);//取消下拉加载
        mAdapter.clearItems();
        mAdapter.addItems(imageList);
    }

    @Override
    public void showImages(List<Image> imageList) {
        //加载数据
        mAdapter.addItems(imageList);
    }

    @Override
    public void loadFail(Throwable t) {
        pullToRefresh.setRefreshing(false);//取消下拉加载
    }

    @Override
    public void noMoreData() {
        pullToRefresh.setRefreshing(false);//取消下拉加载
        showErrorAlert(R.string.no_more_data);
    }
}
