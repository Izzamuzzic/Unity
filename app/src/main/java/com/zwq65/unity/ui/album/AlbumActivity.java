package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.WelfareResponse;
import com.zwq65.unity.ui.base.BaseActivity;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class AlbumActivity extends BaseActivity implements AlbumMvpView {

    @BindView(R.id.rv_albums)
    RecyclerView rvAlbums;

    @Inject
    AlbumMvpPresenter<AlbumMvpView> mPresenter;
    AlbumAdapter adapter;

    int page;

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
        rvAlbums.setLayoutManager(new GridLayoutManager(this, 3));
        rvAlbums.setItemAnimator(new DefaultItemAnimator());
        rvAlbums.addItemDecoration(new MyItemDecoration());
        rvAlbums.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (SCROLL_STATE_SETTLING == newState) {
                    page++;
                    mPresenter.getBeautys(page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        adapter = new AlbumAdapter();
        rvAlbums.setAdapter(adapter);
    }

    public void initData() {
        page = 1;
        mPresenter.getBeautys(page);
    }

    @Override
    public void loadBeatys(WelfareResponse welfareResponse) {
        if (welfareResponse.getResults() != null) {
            adapter.setBeautyList(welfareResponse.getResults());
        }
    }
}
