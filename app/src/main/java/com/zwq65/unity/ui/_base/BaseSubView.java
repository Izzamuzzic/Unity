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

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by janisharali on 27/01/17.
 * 自定义view基类
 */

public abstract class BaseSubView extends ViewGroup implements SubMvpView {

    private BaseContract.View mParentMvpView;

    public BaseSubView(Context context) {
        super(context);
    }

    public BaseSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public BaseSubView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void attachParentMvpView(BaseContract.View mvpView) {
        mParentMvpView = mvpView;
    }

    @Override
    public void showLoading() {
        if (mParentMvpView != null) {
            mParentMvpView.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mParentMvpView != null) {
            mParentMvpView.hideLoading();
        }
    }

    @Override
    public void showErrorAlert(@StringRes int resId) {
        if (mParentMvpView != null) {
            mParentMvpView.showErrorAlert(resId);
        }
    }

    @Override
    public void showSuccessAlert(@StringRes int resId) {
        if (mParentMvpView != null) {
            mParentMvpView.showSuccessAlert(resId);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mParentMvpView != null) {
            mParentMvpView.showMessage(resId);
        }
    }

    protected abstract void bindViewsAndSetOnClickListeners();

    protected abstract void setUp();
}
