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

package com.zwq65.unity.ui._base

import android.support.annotation.StringRes

import com.trello.rxlifecycle2.LifecycleTransformer
import com.zwq65.unity.data.DataManager

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
interface BaseContract {
    /**
     * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
     * pattern must implement. Generally this interface will be extended by a more specific interface
     * that then usually will be implemented by an Activity or Fragment.
     */
    interface View {

        /**
         * 显示loading框
         */
        fun showLoading()

        /**
         * 隐藏loading框
         */
        fun hideLoading()

        /**
         * 显示snackBar消息
         *
         * @param resId StringRes
         */
        fun showMessage(@StringRes resId: Int)

        /**
         * 显示snackBar消息
         *
         * @param message String
         */
        fun showMessage(message: String)

        /**
         * 显示错误消息
         *
         * @param resId StringRes
         */
        fun showError(@StringRes resId: Int)

        /**
         * 显示错误消息
         *
         * @param message String
         */
        fun showError(message: String)


        /**
         * Fragment/Activity中方法,声明在view中;便于在mvp中的presenter里调用;
         *
         * @param <T> T
         * @return ObservableTransformer view层状态为STOP时调用RxLifeCycle来停止[DataManager]事物.
         */
        fun <T> bindUntilStopEvent(): LifecycleTransformer<T>
    }

    /**
     * Every presenter in the app must either implement this interface or extend BasePresenter
     * indicating the MvpView type that wants to be attached with.
     */
    interface Presenter<in V : View> {

        /**
         * 是否还在与view绑定
         *
         * @return boolean
         */
        val isViewAttached: Boolean

        /**
         * attach view时调用此方法
         *
         * @param mvpView View
         */
        fun onAttach(mvpView: V)

        /**
         * detach view时调用此方法
         */
        fun onDetach()
    }

}
