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

import android.app.Application;
import android.content.Context;

import com.zwq65.unity.data.AppDataManager;
import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.db.AppDbHelper;
import com.zwq65.unity.data.db.DbHelper;
import com.zwq65.unity.data.network.ApiHelper;
import com.zwq65.unity.data.network.AppApiHelper;
import com.zwq65.unity.data.network.retrofit.RetrofitApiManager;
import com.zwq65.unity.data.prefs.AppPreferencesHelper;
import com.zwq65.unity.data.prefs.PreferencesHelper;
import com.zwq65.unity.di.ApplicationContext;
import com.zwq65.unity.di.DatabaseInfo;
import com.zwq65.unity.di.PreferenceInfo;
import com.zwq65.unity.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * This is a Dagger module. We use this to bind our Application class as a Context in the AppComponent
 * By using Dagger Android we do not need to pass our Application instance to any module,
 * we simply need to expose our Application as Context.
 * One of the advantages of Dagger.Android is that your
 * Application & Activities are provided into your graph for you.
 * {@link
 * com.zwq65.unity.di.component.ApplicationComponent}.
 */
@Module
public abstract class ApplicationModule {

    @Provides
    @ApplicationContext
    static Context provideContext(Application application) {
        return application;
    }

    @Provides
    static CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @DatabaseInfo
    static String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @PreferenceInfo
    static String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    static DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    static DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    static ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    static RetrofitApiManager provideRetrofitApiManager() {
        return new RetrofitApiManager();
    }

    @Provides
    @Singleton
    static PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

}
