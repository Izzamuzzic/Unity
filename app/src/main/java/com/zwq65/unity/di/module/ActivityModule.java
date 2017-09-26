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
import com.zwq65.unity.ui.account.AccountContract;
import com.zwq65.unity.ui.account.AccountPresenter;
import com.zwq65.unity.ui.account.tabs.collect.TabCollectionContract;
import com.zwq65.unity.ui.account.tabs.collect.TabCollectionPresenter;
import com.zwq65.unity.ui.account.tabs.local.TabLocalContract;
import com.zwq65.unity.ui.account.tabs.local.TabLocalPresenter;
import com.zwq65.unity.ui.album.AlbumContract;
import com.zwq65.unity.ui.album.AlbumPresenter;
import com.zwq65.unity.ui.album.image.ImageContract;
import com.zwq65.unity.ui.album.image.ImagePresenter;
import com.zwq65.unity.ui.article.ArticleContract;
import com.zwq65.unity.ui.article.ArticlePresenter;
import com.zwq65.unity.ui.article.TabArticleContract;
import com.zwq65.unity.ui.article.TabArticlePresenter;
import com.zwq65.unity.ui.login.LoginContract;
import com.zwq65.unity.ui.login.LoginPresenter;
import com.zwq65.unity.ui.main.MainContract;
import com.zwq65.unity.ui.main.MainPresenter;
import com.zwq65.unity.ui.test.TestContract;
import com.zwq65.unity.ui.test.TestPresenter;
import com.zwq65.unity.ui.video.RestVideoContract;
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
    LoginContract.Presenter<LoginContract.View> provideLoginPresenter(
            LoginPresenter<LoginContract.View> loginPresenter) {
        return loginPresenter;
    }

    @Provides
    @PerActivity
    MainContract.Presenter<MainContract.View> provideMainPresenter(
            MainPresenter<MainContract.View> mainPresenter) {
        return mainPresenter;
    }

    @Provides
    @PerActivity
    AlbumContract.Presenter<AlbumContract.View<Image>> provideAlbumPresenter(
            AlbumPresenter<AlbumContract.View<Image>> albumPresenter) {
        return albumPresenter;
    }

    @Provides
    @PerActivity
    ImageContract.Presenter<ImageContract.View> provideImageMvpPresenter(
            ImagePresenter<ImageContract.View> imagePresenter) {
        return imagePresenter;
    }

    @Provides
    @PerActivity
    AccountContract.Presenter<AccountContract.View> providePersonalCenterMvpPresenter(
            AccountPresenter<AccountContract.View> personalCenterPresenter) {
        return personalCenterPresenter;
    }

    @Provides
    @PerActivity
    TabCollectionContract.Presenter<TabCollectionContract.View> provideTabCollectionMvpPresenter(
            TabCollectionPresenter<TabCollectionContract.View> tabCollectionPresenter) {
        return tabCollectionPresenter;
    }

    @Provides
    @PerActivity
    TabLocalContract.Presenter<TabLocalContract.View> provideTabLocalMvpPresenter(
            TabLocalPresenter<TabLocalContract.View> tabLocalPresenter) {
        return tabLocalPresenter;
    }

    @Provides
    @PerActivity
    RestVideoContract.Presenter<RestVideoContract.View<VideoWithImage>> provideRestVideoMvpPresenter(
            RestVideoPresenter<RestVideoContract.View<VideoWithImage>> restVideoPresenter) {
        return restVideoPresenter;
    }

    @Provides
    @PerActivity
    ArticleContract.Presenter<ArticleContract.View> provideIArticlePresenter(
            ArticlePresenter<ArticleContract.View> articlePresenter) {
        return articlePresenter;
    }

    /**
     * 不使用注解{@link com.zwq65.unity.di.PerActivity @PerActivity},因为该presenter作用于多个fragment，不可唯一。
     *
     * @param tabArticlePresenter tabArticlePresenter
     * @return tabArticlePresenter
     */
    @Provides
    TabArticleContract.Presenter<TabArticleContract.View<ArticleWithImage>> provideITabArticlePresenter(
            TabArticlePresenter<TabArticleContract.View<ArticleWithImage>> tabArticlePresenter) {
        return tabArticlePresenter;
    }

    @Provides
    @PerActivity
    TestContract.Presenter<TestContract.View> provideTestMvpPresenter(
            TestPresenter<TestContract.View> testPresenter) {
        return testPresenter;
    }

}
