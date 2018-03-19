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

package com.zwq65.unity.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IntDef;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.ui._base.BaseRefreshFragment;
import com.zwq65.unity.ui.activity.WebArticleActivity;
import com.zwq65.unity.ui.adapter.TabArticleAdapter;
import com.zwq65.unity.ui.contract.TabArticleContract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.inject.Inject;

import static com.zwq65.unity.ui.fragment.TabArticleFragment.Type.android;
import static com.zwq65.unity.ui.fragment.TabArticleFragment.Type.h5;
import static com.zwq65.unity.ui.fragment.TabArticleFragment.Type.ios;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class TabArticleFragment extends BaseRefreshFragment<Article, TabArticleContract.View<Article>,
        TabArticleContract.Presenter<TabArticleContract.View<Article>>> implements TabArticleContract.View<Article> {
    public static final String TECH_TAG = "TECH_TAG";
    @Type
    public int mType;
    @Inject
    TabArticleAdapter<Article> mAdapter;

    public static TabArticleFragment newInstance(@Type int type) {
        Bundle args = new Bundle();
        args.putInt(TECH_TAG, type);
        TabArticleFragment fragment = new TabArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_article;
    }

    @Override
    public void initView() {
        super.initView();
        mAdapter.setOnItemClickListener((article, position) -> gotoDetailActivity(article));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        super.initData(saveInstanceState);
        getType();
        initData();
    }

    /**
     * 获取RecycleView的spanCount
     *
     * @return If orientation is vertical, spanCount is number of columns. If orientation is horizontal, spanCount is number of rows.
     */
    @Override
    public int getSpanCount() {
        return 1;
    }

    @Override
    public void requestDataRefresh() {
        initData();
    }

    @Override
    public void requestDataLoad() {
        getMPresenter().loadDatas(false);
    }

    public void initData() {
        getMPresenter().init();
    }

    private void gotoDetailActivity(Article article) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WebArticleActivity.ARTICLE, article);
        getMActivity().openActivity(WebArticleActivity.class, bundle);
    }

    @Override
    public void refreshData(List<Article> list) {
        super.refreshData(list);
        mAdapter.clearItems();
        mAdapter.addItems(list);
    }

    @Override
    public void loadData(List<Article> list) {
        super.loadData(list);
        mAdapter.addItems(list);
    }

    private void getType() {
        if (getArguments() != null) {
            mType = getArguments().getInt(TECH_TAG, android);
            getMPresenter().setType(mType);
        }
    }

    @IntDef({
            android,
            ios,
            h5
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int android = R.string.android;
        int ios = R.string.ios;
        int h5 = R.string.qianduan;
    }
}
