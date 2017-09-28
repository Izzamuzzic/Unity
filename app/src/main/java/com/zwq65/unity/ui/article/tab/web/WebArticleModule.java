package com.zwq65.unity.ui.article.tab.web;


import com.zwq65.unity.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/28
 * Contact with <zwq651406441@gmail.com>
 * ===============================================
 */
@Module
public abstract class WebArticleModule {
    @ActivityScoped
    @Binds
    abstract WebArticleContract.Presenter<WebArticleContract.View> provideWebArticlePresenter(
            WebArticlePresenter<WebArticleContract.View> webArticlePresenter);
}