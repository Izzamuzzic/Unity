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
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.BaseFragment;
import com.zwq65.unity.ui.base.base_adapter.OnItemClickListener;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.album.imagedetail.ImageActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zwq65.unity.ui.custom.recycleview.XRecyclerView.findMax;

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
    AlbumAdapter adapter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        initView();
        initData();
    }

    public void initView() {
        rvAlbums.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvAlbums.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rvAlbums.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) rvAlbums.getItemAnimator()).setSupportsChangeAnimations(false);
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
        pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        adapter = new AlbumAdapter(mActivity);
        adapter.setOnItemClickListener(new OnItemClickListener<WelfareResponse.Image>() {
            @Override
            public void onClick(WelfareResponse.Image image, int position) {
                startContentActivity(position);
            }
        });
        rvAlbums.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(new MyItemTouchCallBack(adapter));//拖拽监听
        helper.attachToRecyclerView(rvAlbums);
    }

    private void startContentActivity(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(ImageActivity.POSITION, position);
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, (ArrayList<WelfareResponse.Image>) adapter.getData());
        mActivity.openActivity(ImageActivity.class, bundle);
    }

    public void initData() {
        mPresenter.initImages();
    }

    @Override
    public void refreshImages(List<WelfareResponse.Image> imageList) {
        pullToRefresh.setRefreshing(false);//取消下拉加载
        adapter.clear();
        adapter.addAll(imageList);
    }

    @Override
    public void loadImages(List<WelfareResponse.Image> imageList) {
        //加载数据
        adapter.addAll(imageList);
    }

    @Override
    public void loadError(Throwable t) {
        pullToRefresh.setRefreshing(false);//取消下拉加载
    }

    @Override
    public void noMoreData() {
        pullToRefresh.setRefreshing(false);//取消下拉加载
        showErrorAlert(R.string.no_more_data);
    }

    @Override
    public void onDetach() {
        mPresenter.onDetach();
        mPresenter = null;
        super.onDetach();
    }
}
