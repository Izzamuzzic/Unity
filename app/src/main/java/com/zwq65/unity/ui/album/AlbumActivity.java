package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yalantis.phoenix.PullToRefreshView;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui.base.BaseActivity;
import com.zwq65.unity.ui.base.base_adapter.OnItemClickListener;
import com.zwq65.unity.ui.custom.recycleview.LoadingMoreFooter;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.swipe_image.ImageActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends BaseActivity implements AlbumMvpView {

    @BindView(R.id.rv_albums)
    XRecyclerView rvAlbums;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefresh;

    @Inject
    AlbumMvpPresenter<AlbumMvpView> mPresenter;
    AlbumAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    public void initView() {
        rvAlbums.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvAlbums.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rvAlbums.addItemDecoration(new MyItemDecoration());//item间隔
        rvAlbums.setFootView(new LoadingMoreFooter(this));//添加上拉加载动画
        ((DefaultItemAnimator) rvAlbums.getItemAnimator()).setSupportsChangeAnimations(false);
        rvAlbums.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.loadImages(false);
            }
        });
        pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        adapter = new AlbumAdapter(this);
        adapter.setOnItemClickListener(new OnItemClickListener<Image>() {
            @Override
            public void onClick(Image image, int position) {
                startContentActivity(image);
            }
        });
        rvAlbums.setAdapter(adapter);
    }

    private void startContentActivity(Image image) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ImageActivity.IMAGE, image);
        openActivity(ImageActivity.class, bundle);
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
        adapter.addAll(imageList);//加载数据
    }

    @Override
    public void loadError(Throwable t) {
        pullToRefresh.setRefreshing(false);//取消下拉加载
    }

    @Override
    public void noMoreData() {
        pullToRefresh.setRefreshing(false);//取消下拉加载
        onError("没有更多数据了！");
    }
}
