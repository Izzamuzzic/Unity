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

import android.support.annotation.StringRes;

/**
 * ================================================
 * <p> BaseContract
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public interface BaseContract {
    /**
     * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
     * pattern must implement. Generally this interface will be extended by a more specific interface
     * that then usually will be implemented by an Activity or Fragment.
     */
    interface View {

        void showLoading();

        void hideLoading();

        void showMessage(@StringRes int resId);

        void showMessage(String message);

        void showError(@StringRes int resId);

        void showError(String message);
    }

    /**
     * Every presenter in the app must either implement this interface or extend BasePresenter
     * indicating the MvpView type that wants to be attached with.
     */
    interface Presenter<V extends View> {

        void onAttach(V mvpView);

        void onDetach();

        boolean isViewAttached();
    }

}
