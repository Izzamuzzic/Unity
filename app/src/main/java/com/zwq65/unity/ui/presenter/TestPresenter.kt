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
import com.zwq65.unity.data.network.retrofit.response.GankApiResponse
import com.zwq65.unity.data.network.retrofit.response.enity.Image
import com.zwq65.unity.ui._base.BasePresenter
import com.zwq65.unity.ui.contract.TestContract
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * ================================================
 *
 * Created by NIRVANA on 2017/09/13
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class TestPresenter<V : TestContract.View> @Inject
internal constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), TestContract.Presenter<V> {
    /**
     * 加载图片资源
     */
    override fun loadImages() {
        dataManager
                .getRandomImages()
                .apiSubscribe(object : ApiSubscriberCallBack<GankApiResponse<List<Image>>>() {
                    override fun onSuccess(response: GankApiResponse<List<Image>>) {
                        mvpView?.loadData(response.data!!)
                    }

                    override fun onFailure(errCode: String, errMsg: String) {
                    }
                }, true)
    }

    override fun test() {
        Observable.timer(1, TimeUnit.SECONDS)
                .showLoading()
                .subscribe {
                }
    }
}
