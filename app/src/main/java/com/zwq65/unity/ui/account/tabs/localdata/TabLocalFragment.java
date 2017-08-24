package com.zwq65.unity.ui.account.tabs.localdata;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

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
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_tab_local, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        setmPresenter(mPresenter);
        initView();
        return view;
    }

    private void initView() {
        rlLocal.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rlLocal.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rlLocal.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) rlLocal.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new TabLocalAdapter(getContext());
        rlLocal.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        mPresenter.getLocalPictures().subscribe(new Consumer<List<File>>() {
            @Override
            public void accept(@NonNull List<File> files) throws Exception {
                adapter.clearItems();
                adapter.addItems(files);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                showErrorAlert(R.string.load_fail);
            }
        });
    }

}
