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

package com.zwq65.unity.ui._base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.di.component.ActivityComponent;
import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by janisharali on 27/01/17.
 * Fragment基类
 */

public abstract class BaseFragment<V extends MvpView, T extends MvpPresenter<V>> extends Fragment implements MvpView {
    public final String TAG = getClass().getSimpleName();
    public BaseViewActivity mActivity;
    private Unbinder mUnBinder;
    private MvpPresenter mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        LogUtils.i(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreateView");
        View view = inflater.inflate(getLayoutId(), container, false);
        mPresenter = setmPresenter();
        mUnBinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(TAG, "onPause");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        LogUtils.i(TAG, "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseViewActivity) {
            BaseViewActivity activity = (BaseViewActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
        LogUtils.i(TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity.onFragmentDetached(TAG);
        mActivity = null;
        if (mPresenter != null && mPresenter.isViewAttached()) {
            mPresenter.onDetach();
            mPresenter = null;
        }
        LogUtils.i(TAG, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
        LogUtils.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy");
        super.onDestroy();
    }

    public void onToolbarClick() {
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this.getContext());
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showMessage(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showErrorAlert(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showErrorAlert(resId);
        }
    }

    @Override
    public void showErrorAlert(String message) {
        if (mActivity != null) {
            mActivity.showErrorAlert(message);
        }
    }

    @Override
    public void showSuccessAlert(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showSuccessAlert(resId);
        }
    }

    @Override
    public void showSuccessAlert(String message) {
        if (mActivity != null) {
            mActivity.showSuccessAlert(message);
        }
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    /**
     * @return mPresenter
     */
    public abstract T setmPresenter();

    /**
     * @return fragment'ResLayoutId
     */
    @LayoutRes
    public abstract int getLayoutId();


    public abstract void initView();

    public abstract void initData(Bundle saveInstanceState);

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
