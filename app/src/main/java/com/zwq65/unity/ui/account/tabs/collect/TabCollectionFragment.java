package com.zwq65.unity.ui.account.tabs.collect;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by zwq65 on 2017/08/11
 * 收藏图片相册
 */
public class TabCollectionFragment extends BaseFragment implements TabCollectionMvpView {
    @BindView(R.id.rl_collection)
    RecyclerView rlCollection;

    @Inject
    TabCollectionMvpPresenter<TabCollectionMvpView> mPresenter;
    TabCollectionAdapter adapter;

    @Override
    public MvpPresenter setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_collection;
    }

    @Override
    public void initView() {
        rlCollection.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rlCollection.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rlCollection.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) rlCollection.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new TabCollectionAdapter();
        rlCollection.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        mPresenter.getCollectionPictures().subscribe(pictures -> {
            adapter.clearItems();
            adapter.addItems(pictures);
        }, throwable -> showErrorAlert(R.string.error_msg_load_fail));
    }
}
