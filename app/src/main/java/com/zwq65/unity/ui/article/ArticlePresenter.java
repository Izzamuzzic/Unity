package com.zwq65.unity.ui.article;


import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/08/30
 */
public class ArticlePresenter<V extends ArticleContract.IArticleView> extends BasePresenter<V> implements ArticleContract.IArticlePresenter<V> {
    public ArticlePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }
}
