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

package com.zwq65.unity.ui.activity

import android.content.Intent
import android.view.View
import com.zwq65.unity.R
import com.zwq65.unity.ui._base.BaseDaggerActivity
import com.zwq65.unity.ui.contract.LoginContract
import kotlinx.android.synthetic.main.activity_login.*

/**
 * ================================================
 *
 * Created by NIRVANA on 2017/06/29.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LoginActivity : BaseDaggerActivity<LoginContract.View, LoginContract.Presenter<LoginContract.View>>(), LoginContract.View, View.OnClickListener {

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun initBaseTooBar(): Boolean {
        return false
    }

    override fun dealIntent(intent: Intent) {

    }

    override fun initView() {
        btn_login.setOnClickListener(this)
        btn_signup.setOnClickListener(this)
    }

    override fun initData() {

    }

    //跳转到主界面
    override fun openMainActivity() {
        openActivity(MainActivity::class.java)
    }

    override fun openRegisterActivity() {
        openActivity(MainActivity::class.java)
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> mPresenter.login(et_email?.text.toString(), et_password?.text.toString())
            R.id.btn_signup -> {
            }
        }
    }
}
