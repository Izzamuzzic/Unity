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

package com.zwq65.unity.data


import android.content.Context
import com.trello.rxlifecycle2.LifecycleTransformer
import com.zwq65.unity.data.db.DbHelper
import com.zwq65.unity.data.db.model.Picture
import com.zwq65.unity.data.network.ApiHelper
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse
import com.zwq65.unity.data.network.retrofit.response.enity.Article
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.data.network.retrofit.response.enity.Video
import com.zwq65.unity.data.prefs.PreferencesHelper
import com.zwq65.unity.di.ApplicationContext
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ================================================
 * 本地数据、网络访问操作类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Singleton
class AppDataManager @Inject
constructor(@param:ApplicationContext private val mContext: Context,
            private val mPreferencesHelper: PreferencesHelper,
            private val mDbHelper: DbHelper,
            private val mApiHelper: ApiHelper) : DataManager {

    /*******************************************************************************************************
     * AppDataManager
     * *****************************************************************************************************/

    override fun updateApiHeader(userId: Long?, accessToken: String?) {

    }

    /*******************************************************************************************************
     * PreferencesHelper
     * *****************************************************************************************************/

    override var dayNightMode: Boolean?
        get() = mPreferencesHelper.dayNightMode
        set(isNightMode) {
            mPreferencesHelper.dayNightMode = isNightMode
        }

    /*******************************************************************************************************
     * DbHelper
     * *****************************************************************************************************/

    /**
     * 获取用户收藏的图片
     *
     * @return Observable<>
     */
    override val collectionPictures: Observable<List<Picture>>
        get() = mDbHelper.collectionPictures

    override fun savePicture(picture: Picture): Observable<Long> {
        return mDbHelper.savePicture(picture)
    }

    override fun deletePicture(id: String): Observable<Long> {
        return mDbHelper.deletePicture(id)
    }

    override fun isPictureExist(id: String): Observable<Boolean> {
        return mDbHelper.isPictureExist(id)
    }

    /*******************************************************************************************************
     * ApiHelper
     * *****************************************************************************************************/

    /**
     * 获取随机数目的image'list
     *
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun getRandomImages(callBack: ApiSubscriberCallBack<GankApiResponse<List<Image>>>, lifecycleTransformer: LifecycleTransformer<GankApiResponse<List<Image>>>?) {
        mApiHelper.getRandomImages(callBack, lifecycleTransformer)
    }

    /**
     * 获取page页的image'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun get20Images(page: Int, callBack: ApiSubscriberCallBack<GankApiResponse<List<Image>>>, lifecycleTransformer: LifecycleTransformer<GankApiResponse<List<Image>>>?) {
        mApiHelper.get20Images(page, callBack, lifecycleTransformer)
    }

    /**
     * 同时获取相同数量的image和video实例
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun getVideosAndImages(page: Int, callBack: ApiSubscriberCallBack<List<Video>>, lifecycleTransformer: LifecycleTransformer<List<Video>>?) {
        mApiHelper.getVideosAndImages(page, callBack, lifecycleTransformer)
    }

    /**
     * 获取page页的android'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun getAndroidArticles(page: Int, callBack: ApiSubscriberCallBack<List<Article>>, lifecycleTransformer: LifecycleTransformer<List<Article>>?) {
        mApiHelper.getAndroidArticles(page, callBack, lifecycleTransformer)
    }

    /**
     * 获取page页的ios'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun getIosArticles(page: Int, callBack: ApiSubscriberCallBack<List<Article>>, lifecycleTransformer: LifecycleTransformer<List<Article>>?) {
        mApiHelper.getIosArticles(page, callBack, lifecycleTransformer)
    }

    /**
     * 获取page页的前端'list
     *
     * @param page                 页数
     * @param callBack             callBack
     * @param lifecycleTransformer LifecycleTransformer 自动管理生命周期,避免内存泄漏
     */
    override fun getQianduanArticles(page: Int, callBack: ApiSubscriberCallBack<List<Article>>, lifecycleTransformer: LifecycleTransformer<List<Article>>?) {
        mApiHelper.getQianduanArticles(page, callBack, lifecycleTransformer)
    }
}
