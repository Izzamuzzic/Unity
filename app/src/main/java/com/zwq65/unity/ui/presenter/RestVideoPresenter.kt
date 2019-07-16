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
import com.zwq65.unity.data.network.retrofit.response.enity.Video
import com.zwq65.unity.ui._base.BasePresenter
import com.zwq65.unity.ui.contract.RestVideoContract
import javax.inject.Inject

/**
 * ================================================
 *
 * Created by NIRVANA on 2017/08/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class RestVideoPresenter<V : RestVideoContract.View<Video>> @Inject
internal constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), RestVideoContract.Presenter<V> {
    private var page: Int = 0

    override fun init() {
        page = 1
        loadVideos(true)
    }

    override fun loadVideos(isRefresh: Boolean?) {
        dataManager.getVideosAndImages(page)
                .apiSubscribe({
                    it?.let {
                        if (it.isNotEmpty()) {
                            page++
                            if (isRefresh!!) {
                                mvpView?.refreshData(it)
                            } else {
                                mvpView?.loadData(it)
                            }
                        } else {
                            mvpView?.noMoreData()
                        }
                    }
                }, { _: String, errMsg: String ->
                    mvpView?.loadFail(errMsg)
                }
                )
    }
}
