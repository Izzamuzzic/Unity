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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.di.component.ActivityComponent;
import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.LogUtils;

import butterknife.Unbinder;


/**
 * Created by janisharali on 27/01/17.
 * Fragment基类
 */

public abstract class BaseFragment extends Fragment implements MvpView {
    private final String TAG = getClass().getSimpleName();
    private BaseActivity mActivity;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.i(TAG, "onCreateView");
        return initView(inflater, container);
    }

    @Override
    public void onResume() {
        LogUtils.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtils.i(TAG, "onActivityCreated");
        initData(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        LogUtils.i(TAG, "onAttach");
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        LogUtils.i(TAG, "onDetach");
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        LogUtils.i(TAG, "onDestroyView");
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy");
        super.onDestroy();
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
    public boolean isNetworkConnected() {
        if (mActivity != null) {
            return mActivity.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (mActivity != null) {
            mActivity.openActivityOnTokenExpire();
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

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container);

    public abstract void initData(Bundle saveInstanceState);

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
