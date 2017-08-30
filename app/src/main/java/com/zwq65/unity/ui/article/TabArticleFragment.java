package com.zwq65.unity.ui.article;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/30
 */

public class TabArticleFragment extends BaseFragment {
    public static final String TECH_TAG = "tag";
    public String mTag;
    @BindView(R.id.rv_article)
    RecyclerView rvArticle;

    public static TabArticleFragment newInstance(String tag) {
        Bundle args = new Bundle();
        args.putString(TECH_TAG, tag);
        TabArticleFragment fragment = new TabArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_tab_article, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    public void initData(Bundle saveInstanceState) {
        mTag = getArguments().getString(TECH_TAG);
    }

}
