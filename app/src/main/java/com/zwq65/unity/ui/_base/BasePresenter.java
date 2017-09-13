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

/**
 * Created by janisharali on 27/01/17.
 */

import com.zwq65.unity.data.DataManager;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private final DataManager mDataManager;
    private final CompositeDisposable mCompositeDisposable;
    //MvpView接口类型的弱引用
    private Reference<V> mViewRef;

    @Inject
    public BasePresenter(
            DataManager dataManager,
            CompositeDisposable compositeDisposable) {
        this.mDataManager = dataManager;
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mViewRef = new WeakReference<>(mvpView);
    }

    @Override
    public void onDetach() {
        //这里不使用dispose(),而用clear();dispose()之后会阻止一切事务,不可复用;clear()只会停止当前事务,仍可继续复用。
        mCompositeDisposable.clear();
        mViewRef.clear();
        mViewRef = null;
    }
    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public V getMvpView() {
        return mViewRef.get();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

}
