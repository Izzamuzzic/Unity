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

import com.zwq65.unity.di.ActivityContext;
import com.zwq65.unity.di.PerActivity;
import com.zwq65.unity.ui.album.AlbumMvpPresenter;
import com.zwq65.unity.ui.album.AlbumMvpView;
import com.zwq65.unity.ui.album.AlbumPresenter;
import com.zwq65.unity.ui.login.LoginMvpPresenter;
import com.zwq65.unity.ui.login.LoginMvpView;
import com.zwq65.unity.ui.login.LoginPresenter;
import com.zwq65.unity.ui.swipe_image.ImageMvpPresenter;
import com.zwq65.unity.ui.swipe_image.ImageMvpView;
import com.zwq65.unity.ui.swipe_image.ImagePresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by janisharali on 27/01/17.
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
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
            LoginPresenter<LoginMvpView> loginPresenter) {
        return loginPresenter;
    }

    @Provides
    @PerActivity
    AlbumMvpPresenter<AlbumMvpView> provideAlbumPresenter(
            AlbumPresenter<AlbumMvpView> albumPresenter) {
        return albumPresenter;
    }

    @Provides
    @PerActivity
    ImageMvpPresenter<ImageMvpView> provideImageMvpPresenter(
            ImagePresenter<ImageMvpView> imagePresenter) {
        return imagePresenter;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }
}
