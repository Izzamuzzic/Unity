package com.zwq65.unity.ui.article;

import com.zwq65.unity.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Module
public abstract class ArticleModule {
    @FragmentScoped
    @Binds
    abstract ArticleContract.Presenter<ArticleContract.View> provideArticlePresenter(
            ArticlePresenter<ArticleContract.View> articlePresenter);
}
