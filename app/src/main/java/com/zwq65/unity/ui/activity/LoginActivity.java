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

package com.zwq65.unity.ui.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseDaggerActivity;
import com.zwq65.unity.ui.contract.LoginContract;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/06/29.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class LoginActivity extends BaseDaggerActivity<LoginContract.View, LoginContract.Presenter<LoginContract.View>>
        implements LoginContract.View {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.btn_signup)
    TextView btnSignup;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public Boolean initBaseTooBar() {
        return false;
    }

    @Override
    public void dealIntent(Intent intent) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    //跳转到主界面
    @Override
    public void openMainActivity() {
        openActivity(MainActivity.class);
    }

    @Override
    public void openRegisterActivity() {
        openActivity(MainActivity.class);
    }

    @OnClick({R.id.btn_login, R.id.btn_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mPresenter.login(etEmail.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.btn_signup:
                break;
        }
    }
}
