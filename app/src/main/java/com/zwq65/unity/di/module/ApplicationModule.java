/*
 *    Copyright [2017] [NIRVANA PRIVATE LIMITED]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zwq65.unity.di.module;

import android.app.Application;
import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zwq65.unity.data.AppDataManager;
import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.data.db.AppDbHelper;
import com.zwq65.unity.data.db.DbHelper;
import com.zwq65.unity.data.network.ApiHelper;
import com.zwq65.unity.data.network.AppApiHelper;
import com.zwq65.unity.data.network.retrofit.api.GankIoApiService;
import com.zwq65.unity.data.prefs.AppPreferencesHelper;
import com.zwq65.unity.data.prefs.PreferencesHelper;
import com.zwq65.unity.di.ApplicationContext;
import com.zwq65.unity.di.DatabaseInfo;
import com.zwq65.unity.di.PreferenceInfo;
import com.zwq65.unity.utils.AppConstants;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    @PreferenceInfo
    static String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @DatabaseInfo
    static String provideDatabaseName() {
        return AppConstants.DB_NAME;
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
    static PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    static ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    static GankIoApiService provideGankIoApiService(Retrofit retrofit) {
        return retrofit.create(GankIoApiService.class);
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(GankIoApiService.GANK_IO_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);
        return builder.build();
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient() {
        // log用拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                // HeadInterceptor实现了Interceptor，用来往Request Header添加一些业务相关数据，如APP版本，token信息
//                .addInterceptor(new HeadInterceptor())
                .addInterceptor(httpLoggingInterceptor)
                // 连接超时时间设置
                .connectTimeout(20, TimeUnit.SECONDS)
                // 读取超时时间设置
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

}
