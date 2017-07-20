package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.BaseActivity;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends BaseActivity implements AlbumMvpView {

    @BindView(R.id.rv_albums)
    RecyclerView rvAlbums;

    @Inject
    AlbumMvpPresenter<AlbumMvpView> mPresenter;
    AlbumAdapter adapter;
    private int page;
    private List<WelfareResponse.Image> imageList;

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

    public void initView() {
        rvAlbums.setLayoutManager(new LinearLayoutManager(this));//垂直方向两排
        rvAlbums.setItemAnimator(new DefaultItemAnimator());
        rvAlbums.addItemDecoration(new MyItemDecoration());
        rvAlbums.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1
                        && visibleItemCount > 0) {
                    //加载更多
                    mPresenter.loadImages(page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        adapter = new AlbumAdapter(this);
        rvAlbums.setAdapter(adapter);
    }

    public void initData() {
        page = 1;
        imageList = new ArrayList<>();
        mPresenter.loadImages(page);
    }

    @Override
    public void loadImages(List<WelfareResponse.Image> imageList) {
        this.imageList.addAll(imageList);
        page++;
        adapter.setImageList(this.imageList);
    }

    @Override
    public void noMoreData() {
        onError("没有更多数据了！");
    }
}
