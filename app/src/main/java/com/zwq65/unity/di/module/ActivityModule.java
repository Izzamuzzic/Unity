/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.zwq65.unity.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.zwq65.unity.data.network.retrofit.response.enity.ArticleWithImage;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;
import com.zwq65.unity.di.ActivityContext;
import com.zwq65.unity.di.PerActivity;
import com.zwq65.unity.ui.account.AccountMvpPresenter;
import com.zwq65.unity.ui.account.AccountMvpView;
import com.zwq65.unity.ui.account.AccountPresenter;
import com.zwq65.unity.ui.account.tabs.collect.TabCollectionMvpPresenter;
import com.zwq65.unity.ui.account.tabs.collect.TabCollectionMvpView;
import com.zwq65.unity.ui.account.tabs.collect.TabCollectionPresenter;
import com.zwq65.unity.ui.account.tabs.local.TabLocalMvpPresenter;
import com.zwq65.unity.ui.account.tabs.local.TabLocalMvpView;
import com.zwq65.unity.ui.account.tabs.local.TabLocalPresenter;
import com.zwq65.unity.ui.album.AlbumMvpPresenter;
import com.zwq65.unity.ui.album.AlbumMvpView;
import com.zwq65.unity.ui.album.AlbumPresenter;
import com.zwq65.unity.ui.album.image.ImageMvpPresenter;
import com.zwq65.unity.ui.album.image.ImageMvpView;
import com.zwq65.unity.ui.album.image.ImagePresenter;
import com.zwq65.unity.ui.article.ArticleContract;
import com.zwq65.unity.ui.article.ArticlePresenter;
import com.zwq65.unity.ui.article.TabArticleContract;
import com.zwq65.unity.ui.article.TabArticlePresenter;
import com.zwq65.unity.ui.login.LoginMvpPresenter;
import com.zwq65.unity.ui.login.LoginMvpView;
import com.zwq65.unity.ui.login.LoginPresenter;
import com.zwq65.unity.ui.main.MainMvpPresenter;
import com.zwq65.unity.ui.main.MainMvpView;
import com.zwq65.unity.ui.main.MainPresenter;
import com.zwq65.unity.ui.test.TestMvpPresenter;
import com.zwq65.unity.ui.test.TestMvpView;
import com.zwq65.unity.ui.test.TestPresenter;
import com.zwq65.unity.ui.video.RestVideoMvpPresenter;
import com.zwq65.unity.ui.video.RestVideoMvpView;
import com.zwq65.unity.ui.video.RestVideoPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by janisharali on 27/01/17
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
            LoginPresenter<LoginMvpView> loginPresenter) {
        return loginPresenter;
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(
            MainPresenter<MainMvpView> mainPresenter) {
        return mainPresenter;
    }

    @Provides
    @PerActivity
    AlbumMvpPresenter<AlbumMvpView<Image>> provideAlbumPresenter(
            AlbumPresenter<AlbumMvpView<Image>> albumPresenter) {
        return albumPresenter;
    }

    @Provides
    @PerActivity
    ImageMvpPresenter<ImageMvpView> provideImageMvpPresenter(
            ImagePresenter<ImageMvpView> imagePresenter) {
        return imagePresenter;
    }

    @Provides
    @PerActivity
    AccountMvpPresenter<AccountMvpView> providePersonalCenterMvpPresenter(
            AccountPresenter<AccountMvpView> personalCenterPresenter) {
        return personalCenterPresenter;
    }

    @Provides
    @PerActivity
    TabCollectionMvpPresenter<TabCollectionMvpView> provideTabCollectionMvpPresenter(
            TabCollectionPresenter<TabCollectionMvpView> tabCollectionPresenter) {
        return tabCollectionPresenter;
    }

    @Provides
    @PerActivity
    TabLocalMvpPresenter<TabLocalMvpView> provideTabLocalMvpPresenter(
            TabLocalPresenter<TabLocalMvpView> tabLocalPresenter) {
        return tabLocalPresenter;
    }

    @Provides
    @PerActivity
    RestVideoMvpPresenter<RestVideoMvpView<VideoWithImage>> provideRestVideoMvpPresenter(
            RestVideoPresenter<RestVideoMvpView<VideoWithImage>> restVideoPresenter) {
        return restVideoPresenter;
    }

    @Provides
    @PerActivity
    ArticleContract.IArticlePresenter<ArticleContract.IArticleView> provideIArticlePresenter(
            ArticlePresenter<ArticleContract.IArticleView> articlePresenter) {
        return articlePresenter;
    }

    /**
     * 不使用注解{@link com.zwq65.unity.di.PerActivity @PerActivity},因为该presenter作用于多个fragment，不可唯一。
     *
     * @param tabArticlePresenter tabArticlePresenter
     * @return tabArticlePresenter
     */
    @Provides
    TabArticleContract.ITabArticlePresenter<TabArticleContract.ITabArticleView<ArticleWithImage>> provideITabArticlePresenter(
            TabArticlePresenter<TabArticleContract.ITabArticleView<ArticleWithImage>> tabArticlePresenter) {
        return tabArticlePresenter;
    }

    @Provides
    @PerActivity
    TestMvpPresenter<TestMvpView> provideTestMvpPresenter(
            TestPresenter<TestMvpView> testPresenter) {
        return testPresenter;
    }

}
