package com.zwq65.unity.ui.video.watch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.VideoWithImage;
import com.zwq65.unity.ui._base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 视频播放
 */
public class WatchActivity extends BaseActivity {
    //intent'key value
    public static final String VIDEO_WITH_IMAGE = "VideoWithImage";
    VideoWithImage videoWithImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.iv_title_bg)
    ImageView ivTitleBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithoutInject(R.layout.activity_watch);
        setUnBinder(ButterKnife.bind(this));
        initData();
        initView();
    }

    private void initView() {
        //set header'background image
        Glide.with(this).load(videoWithImage.getImage().getUrl()).into(ivTitleBg);
        collapsingToolbarLayout.setTitle(videoWithImage.getVideo().getDesc());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        //setup toolbar
        setSupportActionBar(toolbar);
        //添加‘<--’返回功能
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //添加了toolbar，重新设置沉浸栏
        ImmersionBar.with(this).titleBar(toolbar).init();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        videoWithImage = bundle.getParcelable(VIDEO_WITH_IMAGE);
    }
}
