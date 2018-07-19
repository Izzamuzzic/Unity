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

import com.zwq65.unity.data.DataManager
import com.zwq65.unity.data.network.retrofit.callback.ApiSubscriberCallBack
import com.zwq65.unity.data.network.retrofit.callback.HttpErrorFunction
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import java.lang.ref.WeakReference

import javax.inject.Inject

/**
 * ================================================
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 * <p>
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
open class BasePresenter<V : BaseContract.View> @Inject
constructor(val dataManager: DataManager) : BaseContract.Presenter<V> {
    val TAG = javaClass.simpleName
    /**
     * MvpView接口类型的弱引用
     */
    private var mViewRef: WeakReference<V>? = null

    override val isViewAttached: Boolean
        get() = mViewRef?.get() != null

    val mvpView: V?
        get() = mViewRef?.get()

    override fun onAttach(mvpView: V) {
        mViewRef = WeakReference(mvpView)
    }

    override fun onDetach() {
        mViewRef?.clear()
        mViewRef = null
    }

    /**
     * [Observable]扩展函数 订阅之前网络请求统一调度处理
     *
     * @param callBack             callback回调
     * @param <T>                  返回类型泛型
     */
    fun <T> Observable<T>.apiSubscribe(callBack: ApiSubscriberCallBack<T>) {
        this.compose(getLifeTransformer())
                //简化线程、返回数据处理
                .compose(schedulersTransformer())
                .subscribe(callBack)
    }

    /**
     * [Observable]网络请求时显示加载框，结束后隐藏
     *
     * @param <T>                  返回类型泛型
     */
    fun <T> Observable<T>.showLoading(): Observable<T> {
        return this.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mvpView?.showLoading()
                }
                .doAfterTerminate {
                    mvpView?.hideLoading()
                }
    }

    /**
     * 所有网络请求统一处理(compose简化线程、返回数据处理)
     *
     * @param <T> api返回数据
     * @return api返回数据
    </T> */
    private fun <T> schedulersTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                    //统一切换线程(在io线程进行网络请求；在主线程执行回传ui操作)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //返回数据统一处理(操作失败、异常等情况)
                    .onErrorResumeNext(HttpErrorFunction())
        }

    }

    /**
     * 提供RxLifeCycle的生命周期Transformer
     *
     * @return ObservableTransformer
     */
    private fun <T> getLifeTransformer(): ObservableTransformer<T, T>? {
        return mvpView?.bindUntilStopEvent()
    }

}
