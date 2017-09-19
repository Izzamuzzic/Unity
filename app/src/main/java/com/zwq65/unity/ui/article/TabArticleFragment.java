package com.zwq65.unity.ui.article;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.ArticleWithImage;
import com.zwq65.unity.ui._base.BaseRefreshFragment;
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.article.detail.ArticleDetailActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by zwq65 on 2017/08/30
 */

public class TabArticleFragment extends BaseRefreshFragment<ArticleWithImage> implements TabArticleContract.ITabArticleView<ArticleWithImage> {

    public static final String TECH_TAG = "tag";
    public Type mType;
    TabArticleAdapter mAdapter;
    @Inject
    TabArticleContract.ITabArticlePresenter<TabArticleContract.ITabArticleView<ArticleWithImage>> mPresenter;

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
    public MvpPresenter setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
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
        mAdapter = new TabArticleAdapter();
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

    private void gotoDetailActivity(ArticleWithImage article) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ArticleDetailActivity.ARTICAL, article);
        mActivity.openActivity(ArticleDetailActivity.class, bundle);
    }

    @Override
    public void refreshData(List<ArticleWithImage> list) {
        super.refreshData(list);
        mAdapter.clearItems();
        mAdapter.addItems(list);
    }

    @Override
    public void loadData(List<ArticleWithImage> list) {
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
