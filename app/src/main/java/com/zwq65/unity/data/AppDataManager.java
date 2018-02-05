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

package com.zwq65.unity.data;


import android.content.Context;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.zwq65.unity.data.db.DbHelper;
import com.zwq65.unity.data.db.model.Picture;
import com.zwq65.unity.data.network.ApiHelper;
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack;
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse;
import com.zwq65.unity.data.network.retrofit.response.enity.Article;
import com.zwq65.unity.data.network.retrofit.response.enity.Image;
import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.data.prefs.PreferencesHelper;
import com.zwq65.unity.di.ApplicationContext;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * ================================================
 * 本地数据、网络访问操作类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
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
    public Observable<Long> savePicture(Picture picture) {
        return mDbHelper.savePicture(picture);
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

    /**
     * 获取随机数目的image'list
     *
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void getRandomImages(ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, LifecycleTransformer<GankApiResponse<List<Image>>> lifecycleTransformer) {
        mApiHelper.getRandomImages(callBack, lifecycleTransformer);
    }

    /**
     * 获取page页的image'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void get20Images(int page, ApiSubscriberCallBack<GankApiResponse<List<Image>>> callBack, LifecycleTransformer<GankApiResponse<List<Image>>> lifecycleTransformer) {
        mApiHelper.get20Images(page, callBack, lifecycleTransformer);
    }

    /**
     * 同时获取相同数量的image和video实例
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void getVideosAndImages(int page, ApiSubscriberCallBack<List<Video>> callBack, LifecycleTransformer<List<Video>> lifecycleTransformer) {
        mApiHelper.getVideosAndImages(page, callBack, lifecycleTransformer);
    }

    /**
     * 获取page页的android'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void getAndroidArticles(int page, ApiSubscriberCallBack<List<Article>> callBack, LifecycleTransformer<List<Article>> lifecycleTransformer) {
        mApiHelper.getAndroidArticles(page, callBack, lifecycleTransformer);
    }

    /**
     * 获取page页的ios'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void getIosArticles(int page, ApiSubscriberCallBack<List<Article>> callBack, LifecycleTransformer<List<Article>> lifecycleTransformer) {
        mApiHelper.getIosArticles(page, callBack, lifecycleTransformer);
    }

    /**
     * 获取page页的前端'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    @Override
    public void getQianduanArticles(int page, ApiSubscriberCallBack<List<Article>> callBack, LifecycleTransformer<List<Article>> lifecycleTransformer) {
        mApiHelper.getQianduanArticles(page, callBack, lifecycleTransformer);
    }
}
