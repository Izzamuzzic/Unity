package com.zwq65.unity.ui.article;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by zwq65 on 2017/08/30
 */
public class ArticleActivity extends BaseActivity implements ArticleContract.IArticleView {

    @Inject
    ArticleContract.IArticlePresenter<ArticleContract.IArticleView> mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_article);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
    }
}
