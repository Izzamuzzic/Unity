package com.zwq65.unity.ui.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends BaseActivity implements AlbumMvpView {

    @BindView(R.id.rv_albums)
    RecyclerView rvAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        setUnBinder(ButterKnife.bind(this));
        initView();
    }

    private void initView() {
        rvAlbums.setLayoutManager(new GridLayoutManager(this, 3));
        rvAlbums.setItemAnimator(new DefaultItemAnimator());
        List<String> numList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            numList.add(i + "");
        }
        AlbumAdapter adapter = new AlbumAdapter();
        rvAlbums.setAdapter(adapter);
        adapter.setNumList(numList);
    }

}
