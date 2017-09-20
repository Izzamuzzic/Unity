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

package com.zwq65.unity.data;


import android.content.Context;

import com.zwq65.unity.data.db.DbHelper;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.data.network.ApiHelper;
import com.zwq65.unity.data.network.retrofit.callback.ApiErrorCallBack;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.ArticleWithImage;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.VideoWithImage;
import com.zwq65.unity.data.prefs.PreferencesHelper;
import com.zwq65.unity.di.ApplicationContext;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by janisharali on 27/01/17.
 * 本地数据操作类
 */

@Singleton
public class AppDataManager implements DataManager {

    private final Context mContext;
    private final DbHelper mDbHelper;
    private final PreferencesHelper mPreferencesHelper;
    private final ApiHelper mApiHelper;


    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          PreferencesHelper preferencesHelper,
                          DbHelper dbHelper,
                          ApiHelper apiHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mDbHelper = dbHelper;
        mApiHelper = apiHelper;
    }

    /***************************************************  AppDataManager  **************************************************************/

    @Override
    public void updateApiHeader(Long userId, String accessToken) {

    }

    /***************************************************  PreferencesHelper  ************************************************************/

    @Override
    public Boolean getDayNightmode() {
        return mPreferencesHelper.getDayNightmode();
    }

    @Override
    public void setDayNightmode(Boolean isNightmode) {
        mPreferencesHelper.setDayNightmode(isNightmode);
    }

    /*****************************************************  DbHelper  *****************************************************************/

    @Override
    public Observable<Long> insertPicture(Picture picture) {
        return mDbHelper.insertPicture(picture);
    }

    @Override
    public Observable<Long> deletePicture(String id) {
        return mDbHelper.deletePicture(id);
    }

    @Override
    public Observable<Boolean> isPictureExist(String id) {
        return mDbHelper.isPictureExist(id);
    }

    @Override
    public Observable<List<Picture>> getCollectionPictures() {
        return mDbHelper.getCollectionPictures();
    }

    /*****************************************************  ApiHelper  *****************************************************************/

    @Override
    public Disposable getRandomImages(ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return mApiHelper.getRandomImages(callBack, errorCallBack);
    }

    @Override
    public Disposable get20Images(int page, ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return mApiHelper.get20Images(page, callBack, errorCallBack);
    }

    @Override
    public Disposable getVideosAndIMages(int page, ApiSubscriberCallBack<List<VideoWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return mApiHelper.getVideosAndIMages(page, callBack, errorCallBack);
    }

    @Override
    public Disposable getAndroidArticles(int page, ApiSubscriberCallBack<List<ArticleWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return mApiHelper.getAndroidArticles(page, callBack, errorCallBack);
    }

    @Override
    public Disposable getIosArticles(int page, ApiSubscriberCallBack<List<ArticleWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return mApiHelper.getIosArticles(page, callBack, errorCallBack);
    }

    @Override
    public Disposable getQianduanArticles(int page, ApiSubscriberCallBack<List<ArticleWithImage>> callBack, ApiErrorCallBack<Throwable> errorCallBack) {
        return mApiHelper.getQianduanArticles(page, callBack, errorCallBack);
    }

}
