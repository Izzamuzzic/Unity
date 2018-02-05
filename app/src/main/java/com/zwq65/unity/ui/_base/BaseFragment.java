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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zwq65.unity.data.DataManager;
import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * ================================================
 * Fragment基类
 * A {@link Fragment} that injects its members in {@link #onAttach(Context)} and can be used to
 * inject child {@link Fragment}s attached to it. Note that when this fragment gets reattached, its
 * members will be injected again.
 * <p>
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public abstract class BaseFragment<V extends BaseContract.View, T extends BaseContract.Presenter<V>> extends RxFragment
        implements HasSupportFragmentInjector, BaseContract.View {
    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    public final String TAG = getClass().getSimpleName();
    public BaseDaggerActivity mActivity;
    private Unbinder mUnBinder;
    private ProgressDialog mProgressDialog;

    @Inject
    public T mPresenter;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

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
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        //init mActivity
        if (context instanceof BaseDaggerActivity) {
            BaseDaggerActivity activity = (BaseDaggerActivity) context;
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
     * Fragment/Activity中方法,声明在view中;便于在mvp中的presenter里调用;
     *
     * @param <T> T
     * @return ObservableTransformer view层状态为STOP时调用RxLifeCycle来停止{@link DataManager}事物.
     */
    @Override
    public <T> LifecycleTransformer<T> bindUntilStopEvent() {
        return bindUntilEvent(FragmentEvent.STOP);
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
