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

package com.zwq65.unity.ui.presenter

import com.zwq65.unity.data.DataManager
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack
import com.zwq65.unity.data.network.retrofit.response.enity.Article
import com.zwq65.unity.ui._base.BasePresenter
import com.zwq65.unity.ui.contract.TabArticleContract
import com.zwq65.unity.ui.fragment.TabArticleFragment

import javax.inject.Inject

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/31
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class TabArticlePresenter<V : TabArticleContract.View<Article>> @Inject
internal constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), TabArticleContract.Presenter<V> {
    @TabArticleFragment.Type
    private var type: Int = 0
    private var page: Int = 0
    private var isRefresh: Boolean = false

    private val apiSubscriberCallBack: ApiSubscriberCallBack<List<Article>>
        get() = object : ApiSubscriberCallBack<List<Article>>() {
            override fun onSuccess(response: List<Article>) {
                if (response.isNotEmpty()) {
                    page++
                    if (isRefresh) {
                        mvpView?.refreshData(response)
                    } else {
                        mvpView?.loadData(response)
                    }
                } else {
                    mvpView?.noMoreData()
                }
            }

            override fun onFailure(errCode: String, errMsg: String) {
                mvpView?.loadFail(errMsg)
            }
        }

    override fun setType(@TabArticleFragment.Type type: Int) {
        this.type = type
    }

    override fun init() {
        page = 1
        loadDatas(true)
    }

    override fun loadDatas(isRefresh: Boolean?) {
        this.isRefresh = isRefresh!!
        when (type) {
            TabArticleFragment.ANDROID -> dataManager.getAndroidArticles(page).apiSubscribe(apiSubscriberCallBack)
            TabArticleFragment.IOS -> dataManager.getIosArticles(page).apiSubscribe(apiSubscriberCallBack)
            TabArticleFragment.H5 -> dataManager.getQianduanArticles(page).apiSubscribe(apiSubscriberCallBack)
            else -> {
            }
        }
    }

}
