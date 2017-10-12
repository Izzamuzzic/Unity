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
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

/**
 * ================================================
 * Fragment基类
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public abstract class BaseFragment<V extends BaseContract.View, T extends BaseContract.Presenter<V>> extends DaggerFragment
        implements BaseContract.View {
    public final String TAG = getClass().getSimpleName();
    public BaseViewActivity mActivity;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;

    @Inject
    public T mPresenter;

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
        //init mActivity
        if (context instanceof BaseViewActivity) {
            BaseViewActivity activity = (BaseViewActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
        //Presenter attach the view
        if (mPresenter != null) {
            mPresenter.onAttach((V) this);
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
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy");
    }

    public void onToolbarClick() {
    }

    @Override
    public void showLoading() {
        hideLoading();
        if (mActivity != null) {
            mProgressDialog = CommonUtils.showLoadingDialog(mActivity);
        }
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
    public void showError(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showError(resId);
        }
    }

    @Override
    public void showError(String message) {
        if (mActivity != null) {
            mActivity.showError(message);
        }
    }

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
