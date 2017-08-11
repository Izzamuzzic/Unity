/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.zwq65.unity.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jingewenku.abrahamcaijin.commonutil.AppNetworkMgr;
import com.jingewenku.abrahamcaijin.commonutil.AppScreenMgr;
import com.tapadoo.alerter.Alerter;
import com.zwq65.unity.R;
import com.zwq65.unity.UnityApp;
import com.zwq65.unity.di.component.ActivityComponent;
import com.zwq65.unity.di.component.DaggerActivityComponent;
import com.zwq65.unity.di.module.ActivityModule;
import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by janisharali on 27/01/17
 * activity基类
 */

public class BaseActivity extends AppCompatActivity implements MvpView, BaseFragment.Callback {

    private final String TAG = getClass().getSimpleName();
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.ivLogo)
    ImageView ivLogo;

    private ProgressDialog mProgressDialog;
    private ActivityComponent mActivityComponent;
    private Unbinder mUnBinder;//An unbinder contract that will unbind views when called
    private FragmentManager fragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
        //不含toolbar的activity，采用fitsSystemWindows(false)实现沉浸式
        ImmersionBar.with(this).fitsSystemWindows(false).init();
        int statusHeight = AppScreenMgr.getStatusHeight(this);
        LogUtils.i("statusHeight：" + statusHeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    protected void bindViews() {
        ButterKnife.bind(this);
        setupToolbar();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_menu_white);
            //含toolbar的activity，实现沉浸式状态栏
            ImmersionBar.with(this).titleBar(toolbar).init();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((UnityApp) getApplication()).getComponent())
                .build();
    }

    @Override
    protected void onResume() {
        LogUtils.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtils.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LogUtils.i(TAG, "onDestroy");
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
        ImmersionBar.with(this).destroy();//不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        //添加intentFlag,如果要启动的activity存在于栈中,将其拉到栈顶,不用重新实例化
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
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
    public void showErrorAlert(@StringRes int resId) {
        showErrorAlert(getString(resId));
    }

    @Override
    public void showSuccessAlert(@StringRes int resId) {
        showSuccessAlert(getString(resId));
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public void showErrorAlert(String message) {
        Alerter.create(this)
                .setBackgroundColorRes(R.color.red_alert)
                .enableSwipeToDismiss()
                .setDuration(1000)
                .setTitle("出错了")
                .setText(message)
                .show();
    }

    @Override
    public void showSuccessAlert(String message) {
        Alerter.create(this)
                .setBackgroundColorRes(R.color.colorAccent)
                .enableSwipeToDismiss()
                .setDuration(1000)
                .setTitle("提示")
                .setText(message)
                .show();
    }

    @Override
    public void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public boolean isNetworkConnected() {
        return AppNetworkMgr.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    Fragment currentFragment;

    /**
     * 切换fragment
     * hide当前显示的fragment，若已添加,show(),否则add()
     *
     * @param containerViewId 容器view
     * @param targetFragment  要切换的fragment
     * @param tag             fragment'tag
     */
    public void switchFragment(int containerViewId, Fragment targetFragment, String tag) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment exsitFragment = fragmentManager.findFragmentByTag(tag);
        if (exsitFragment != null) {
            //已添加( ⊙o⊙ ),show()
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.show(exsitFragment).commit();
            currentFragment = exsitFragment;
        } else {
            //还没添加呢,add()
            if (!targetFragment.isAdded()) {
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.add(containerViewId, targetFragment, tag).commit();
            } else {
                //已添加( ⊙o⊙ ),show()
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                transaction.show(targetFragment).commit();
            }
            currentFragment = targetFragment;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
//        startActivity(LoginActivity.getStartIntent(this));
//        finish();
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

}
