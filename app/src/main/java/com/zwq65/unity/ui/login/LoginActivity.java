package com.zwq65.unity.ui.login;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zwq65.unity.R;
import com.zwq65.unity.ui._base.BaseViewActivity;
import com.zwq65.unity.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends BaseViewActivity<LoginMvpView, LoginMvpPresenter<LoginMvpView>> implements LoginMvpView {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.btn_signup)
    TextView btnSignup;

    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;

    @Override
    public LoginMvpPresenter<LoginMvpView> setmPresenter() {
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        return mPresenter;
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public Boolean initBaseTooBar() {
        return true;
    }

    @Override
    public Unbinder setUnBinder() {
        return ButterKnife.bind(this);
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
