package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.yalantis.phoenix.PullToRefreshView;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse.Image;
import com.zwq65.unity.ui.base.BaseActivity;
import com.zwq65.unity.ui.base.base_adapter.OnItemClickListener;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.swipe_image.ImageActivity;
import com.zwq65.unity.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zwq65.unity.ui.custom.recycleview.XRecyclerView.findMax;

public class AlbumActivity extends BaseActivity implements AlbumMvpView {

    @BindView(R.id.rv_albums)
    RecyclerView rvAlbums;
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
        ((DefaultItemAnimator) rvAlbums.getItemAnimator()).setSupportsChangeAnimations(false);
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
                        LogUtils.i("onLoadMore");
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
        adapter = new AlbumAdapter(this);
        adapter.setOnItemClickListener(new OnItemClickListener<Image>() {
            @Override
            public void onClick(Image image, int position) {
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
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, (ArrayList<Image>) adapter.getData());
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
        showErrorAlert("没有更多数据了！");
    }
}
