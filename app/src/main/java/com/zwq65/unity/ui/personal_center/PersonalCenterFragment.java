package com.zwq65.unity.ui.personal_center;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.ui.base.BaseFragment;
import com.zwq65.unity.ui.custom.recycleview.MyItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/07
 */

public class PersonalCenterFragment extends BaseFragment implements PersonalCenterMvpView {

    @Inject
    PersonalCenterMvpPresenter<PersonalCenterMvpView> mPresenter;
    @BindView(R.id.rl_abbreviated_collections)
    RecyclerView rlAbbreviatedCollections;

    CollectionAdapter adapter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getCollectionPhoto();
    }

    private void initView() {
        rlAbbreviatedCollections.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        rlAbbreviatedCollections.setItemAnimator(new DefaultItemAnimator());//item加载动画（默认）
        rlAbbreviatedCollections.addItemDecoration(new MyItemDecoration());
        adapter = new CollectionAdapter(mActivity);
        rlAbbreviatedCollections.setAdapter(adapter);
    }


    @Override
    public void showCollection(List<Picture> pictures) {
        adapter.clear();
        adapter.addAll(pictures);
    }

    @Override
    public void onDetach() {
        mPresenter.onDetach();
        super.onDetach();
    }
}
