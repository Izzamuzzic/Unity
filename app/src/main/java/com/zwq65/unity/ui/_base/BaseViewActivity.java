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

package com.zwq65.unity.ui._base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zwq65.unity.R;
import com.zwq65.unity.utils.CommonUtils;

import javax.inject.Inject;

/**
 * Created by zwq65 on 2017/09/12
 */

public abstract class BaseViewActivity<V extends BaseContract.View, P extends BaseContract.Presenter<V>>
        extends BaseActivity implements BaseContract.View, BaseFragment.Callback {
    Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    @Inject
    public P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onAttach((V) this);
        }
        if (initBaseTooBar() != null && initBaseTooBar()) {
            setupBaseToolbar();
        } else {
            //不含toolbar的activity，采用fitsSystemWindows(false)方式实现沉浸栏
            ImmersionBar.with(this).fitsSystemWindows(false).init();
        }
        if (getIntent() != null) {
            dealIntent(getIntent());
        }
        initView();
        initData();
    }

    /**
     * @return 是否加载BaseToolBar
     */
    public abstract Boolean initBaseTooBar();

    /**
     * @param intent 获取到的Intent
     */
    public abstract void dealIntent(Intent intent);

    /**
     * 初始化View
     * 进行findViewById
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    protected void setupBaseToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_menu_white);
            //含toolbar的activity，实现沉浸式状态栏
            ImmersionBar.with(this).titleBar(toolbar).init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
        //解绑presenter
        if (mPresenter != null && mPresenter.isViewAttached()) {
            mPresenter.onDetach();
            mPresenter = null;
        }
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void showError(@StringRes int resId) {
        showError(getString(resId));
    }

    @Override
    public void showError(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.warn_color));
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    public <T> T $(int _id) {
        return (T) findViewById(_id);
    }
}
