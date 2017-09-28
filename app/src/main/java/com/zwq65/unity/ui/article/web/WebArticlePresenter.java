package com.zwq65.unity.ui.article.web;


import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.ui._base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/28
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class WebArticlePresenter<V extends WebArticleContract.View> extends BasePresenter<V>
        implements WebArticleContract.Presenter<V> {
    @Inject
    WebArticlePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }
}
