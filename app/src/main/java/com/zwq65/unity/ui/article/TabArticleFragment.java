package com.zwq65.unity.ui.article;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.phoenix.PullToRefreshView;
import com.zwq65.unity.R;
import com.zwq65.unity.data.network.retrofit.response.enity.ArticleWithImage;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;
import com.zwq65.unity.ui.article.detail.ArticleDetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zwq65.unity.ui._custom.recycleview.XRecyclerView.findMax;

/**
 * Created by zwq65 on 2017/08/30
 */

public class TabArticleFragment extends BaseFragment<TabArticleContract.ITabArticleView, TabArticleContract.ITabArticlePresenter<TabArticleContract.ITabArticleView>>
        implements TabArticleContract.ITabArticleView {
    public static final String TECH_TAG = "tag";
    public Type mType;
    TabArticleAdapter mAdapter;
    @Inject
    TabArticleContract.ITabArticlePresenter<TabArticleContract.ITabArticleView> mPresenter;

    @BindView(R.id.rv_article)
    RecyclerView rvArticle;
    @BindView(R.id.pull_to_refresh)
    PullToRefreshView pullToRefresh;

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

    public static TabArticleFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(TECH_TAG, type);
        TabArticleFragment fragment = new TabArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public TabArticleContract.ITabArticlePresenter<TabArticleContract.ITabArticleView> setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
    }

    @Override
    public View inflateLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_article, container, false);
    }

    @Override
    public Unbinder setUnBinder(View view) {
        return ButterKnife.bind(this, view);
    }

    @Override
    public void initView() {
        rvArticle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvArticle.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rvArticle.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) rvArticle.getItemAnimator()).setSupportsChangeAnimations(false);
        //上拉刷新監聽
        pullToRefresh.setOnRefreshListener(this::initData);
        //下拉加載監聽
        rvArticle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = rvArticle.getLayoutManager();
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
                        //onLoadMore
                        mPresenter.loadDatas(false);
                    }
                }
            }
        });
        mAdapter = new TabArticleAdapter(getContext());
        mAdapter.setOnItemClickListener((article, position) -> gotoDetailActivity(article));
        rvArticle.setAdapter(mAdapter);
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        getType();
        initData();
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
    public void refreshData(List<ArticleWithImage> t) {
        pullToRefresh.setRefreshing(false);//取消下拉加载
        mAdapter.clearItems();
        mAdapter.addItems(t);
    }

    @Override
    public void showData(List<ArticleWithImage> t) {
        mAdapter.addItems(t);
    }

    @Override
    public void noMoreData() {
        pullToRefresh.setRefreshing(false);//取消下拉加载
        showErrorAlert(R.string.no_more_data);
    }

    private void getType() {
        int type = getArguments().getInt(TECH_TAG);
        switch (type) {
            case R.string.android:
                mType = Type.Android;
                break;
            case R.string.ios:
                mType = Type.Ios;
                break;
            case R.string.qianduan:
                mType = Type.Qianduan;
                break;
            default:
                mType = Type.Android;
                break;
        }
        mPresenter.setType(mType);
    }

}
