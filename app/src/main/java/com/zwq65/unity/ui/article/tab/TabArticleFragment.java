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

package com.zwq65.unity.ui.article.tab;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.ui._base.BaseRefreshFragment;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.article.tab.web.WebArticleActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class TabArticleFragment extends BaseRefreshFragment<Article, TabArticleContract.View<Article>,
        TabArticleContract.Presenter<TabArticleContract.View<Article>>> implements TabArticleContract.View<Article> {

    public static final String TECH_TAG = "tag";
    public Type mType;
    @Inject
    TabArticleAdapter<Article> mAdapter;

    enum Type {
        Android(R.string.android),
        Ios(R.string.ios),
        Qianduan(R.string.qianduan);
        private int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static TabArticleFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(TECH_TAG, type);
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        mRecyclerView.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter.setOnItemClickListener((article, position) -> gotoDetailActivity(article));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        super.initData(saveInstanceState);
        getType();
        initData();
    }

    @Override
    public void requestDataRefresh() {
        initData();
    }

    @Override
    public void requestDataLoad() {
        mPresenter.loadDatas(false);
    }

    public void initData() {
        mPresenter.init();
    }

    private void gotoDetailActivity(Article article) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WebArticleActivity.ARTICAL, article);
        mActivity.openActivity(WebArticleActivity.class, bundle);
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
        String type = getArguments().getString(TECH_TAG);
        assert type != null;
        switch (type) {
            case "Android":
                mType = Type.Android;
                break;
            case "Ios":
                mType = Type.Ios;
                break;
            case "前端":
                mType = Type.Qianduan;
                break;
            default:
                mType = Type.Android;
                break;
        }
        mPresenter.setType(mType);
    }

}
