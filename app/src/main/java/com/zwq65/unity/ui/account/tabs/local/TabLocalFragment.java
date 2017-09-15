package com.zwq65.unity.ui.account.tabs.local;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui._base.MvpPresenter;
import com.zwq65.unity.ui._custom.recycleview.MyItemDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zwq65 on 2017/08/22
 * 本地保存相册
 */

public class TabLocalFragment extends BaseFragment implements TabLocalMvpView {

    @Inject
    TabLocalMvpPresenter<TabLocalMvpView> mPresenter;
    @BindView(R.id.rl_local)
    RecyclerView rlLocal;
    TabLocalAdapter adapter;

    @Override
    public MvpPresenter setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
    }

    @Override
    public View inflateLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_tab_local, container, false);
    }

    @Override
    public Unbinder setUnBinder(View view) {
        return ButterKnife.bind(this, view);
    }

    @Override
    public void initView() {
        rlLocal.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rlLocal.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rlLocal.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) rlLocal.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new TabLocalAdapter();
        rlLocal.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        mPresenter.getLocalPictures().subscribe(files -> {
            adapter.clearItems();
            adapter.addItems(files);
        }, throwable -> showErrorAlert(R.string.error_msg_load_fail));
    }

}
