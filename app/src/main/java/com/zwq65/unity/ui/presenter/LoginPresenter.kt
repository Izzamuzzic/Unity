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
import com.zwq65.unity.ui._base.BasePresenter
import com.zwq65.unity.ui.contract.LoginContract

import javax.inject.Inject

/**
 * ================================================
 *
 * Created by NIRVANA on 2017/06/29.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LoginPresenter<V : LoginContract.View> @Inject
internal constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), LoginContract.Presenter<V> {

    override fun login(account: String, password: String) {
        //        if (TextUtils.isEmpty(account)) {
        //            getMvpView().onError("请输入账号");
        //            return;
        //        }
        //        if (TextUtils.isEmpty(password)) {
        //            getMvpView().onError("请输入密码");
        //            return;
        //        }
        mvpView!!.openMainActivity()
    }

    override fun register() {

    }

    override fun forgotPsd() {

    }

}
