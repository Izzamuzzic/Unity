package com.zwq65.unity.ui.account.tabs.collection;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui._base.BaseFragment;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_tab_collection, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        setmPresenter(mPresenter);
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        initView();
        mPresenter.getCollectionPictures();
    }

    private void initView() {
        rlCollection.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rlCollection.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rlCollection.addItemDecoration(new MyItemDecoration());//item间隔
        ((DefaultItemAnimator) rlCollection.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new TabCollectionAdapter(getContext());
        rlCollection.setAdapter(adapter);

    }

    @Override
    public void showCollectionPictures(List<Picture> pictures) {
        adapter.clear();
        adapter.addAll(pictures);
    }
}
