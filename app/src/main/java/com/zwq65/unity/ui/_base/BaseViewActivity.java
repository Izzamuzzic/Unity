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
import com.tapadoo.alerter.Alerter;
import com.zwq65.unity.R;
import com.zwq65.unity.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zwq65 on 2017/09/12
 */

public abstract class BaseViewActivity<V extends MvpView, T extends MvpPresenter<V>> extends BaseActivity implements MvpView, BaseFragment.Callback {
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected T mPresenter;
    private ProgressDialog mProgressDialog;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        if (initBaseTooBar() != null && initBaseTooBar()) {
            setupBaseToolbar();
        } else {
            //不含toolbar的activity，采用fitsSystemWindows(false)实现沉浸式
            ImmersionBar.with(this).fitsSystemWindows(false).init();
        }
        mPresenter = setmPresenter();
        mUnBinder = setUnBinder();
        if (getIntent() != null) {
            dealIntent(getIntent());
        }
        initView();
        initData();
    }

    /**
     * @return mPresenter
     */
    public abstract T setmPresenter();

    /**
     * @return LayoutId
     */
    public abstract int setLayoutId();

    /**
     * @return 是否加载BaseToolBar
     */
    public abstract Boolean initBaseTooBar();


    /**
     * @return mUnBinder(An unbinder contract that will unbind views when called)
     */
    public abstract Unbinder setUnBinder();

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
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_menu_white);
            //含toolbar的activity，实现沉浸式状态栏
            ImmersionBar.with(this).titleBar(toolbar).init();
        }
    }

    @Override
    protected void onDestroy() {
        //视图销毁后，mPresenter.onDetach(); mUnBinder.unbind();
        if (mPresenter != null && mPresenter.isViewAttached()) {
            mPresenter.onDetach();
            mPresenter = null;
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
        super.onDestroy();
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
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
                .setBackgroundColorRes(R.color.warn_color)
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
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
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
}