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

package com.zwq65.unity.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.ui._base.BaseDaggerActivity;
import com.zwq65.unity.ui.contract.WatchContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ================================================
 * 视频播放
 * <p>
 * Created by NIRVANA on 2017/06/29.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class WatchActivity extends BaseDaggerActivity<WatchContract.View, WatchContract.Presenter<WatchContract.View>>
        implements WatchContract.View {
    //intent'key value
    public static final String VIDEO = "VIDEO";
    Video video;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.iv_title_bg)
    ImageView ivTitleBg;

    @Override
    public int getLayoutId() {
        return R.layout.activity_watch;
    }

    @Override
    public Boolean initBaseTooBar() {
        return null;
    }

    @Override
    public void dealIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        video = bundle.getParcelable(VIDEO);
    }

    @Override
    public void initView() {
        //set header'background image
        if (video != null) {
            Glide.with(this).load(video.getImage().getUrl()).into(ivTitleBg);
            collapsingToolbarLayout.setTitle(video.getDesc());
        }
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        //setup toolbar
        setSupportActionBar(toolbar);
        //添加‘<--’返回功能
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> WatchActivity.this.onBackPressed());
        //添加了toolbar，重新设置沉浸栏
        ImmersionBar.with(this).titleBar(toolbar).init();
    }

    @Override
    public void initData() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_collect_bg_image:
                //查看背景图
                gotoContentActivity();
                break;
            default:
                break;
        }
        return true;
    }

    private void gotoContentActivity() {
        List<Image> images = new ArrayList<>(1);
        images.add(video.getImage());
        Bundle bundle = new Bundle();
        bundle.putInt(ImageActivity.POSITION, 0);
        bundle.putParcelableArrayList(ImageActivity.IMAGE_LIST, (ArrayList<Image>) images);
        openActivity(ImageActivity.class, bundle);
    }

}
