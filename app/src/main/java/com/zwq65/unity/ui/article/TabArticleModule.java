package com.zwq65.unity.ui.article;

import com.zwq65.unity.data.network.retrofit.response.enity.ArticleWithImage;

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
public abstract class TabArticleModule {
    /**
     * 不使用注解{@link com.zwq65.unity.di.FragmentScoped @FragmentScoped},因为该presenter作用于多个fragment，不可唯一。
     *
     * @param tabArticlePresenter tabArticlePresenter
     * @return tabArticlePresenter
     */
    @Binds
    abstract TabArticleContract.Presenter<TabArticleContract.View<ArticleWithImage>> provideTabArticlePresenter(
            TabArticlePresenter<TabArticleContract.View<ArticleWithImage>> tabArticlePresenter);
}
