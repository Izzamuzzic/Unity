package com.zwq65.unity.ui.article;


import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zwq65 on 2017/08/30
 */
public class ArticlePresenter<V extends ArticleContract.View> extends BasePresenter<V> implements ArticleContract.Presenter<V> {
    @Inject
    ArticlePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }
}
