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

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import com.zwq65.unity.data.DataManager
import com.zwq65.unity.utils.CommonUtils
import com.zwq65.unity.utils.LogUtils
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * ================================================
 * Fragment基类
 * A [Fragment] that injects its members in [.onAttach] and can be used to
 * inject child [Fragment]s attached to it. Note that when this fragment gets reattached, its
 * members will be injected again.
 *
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
abstract class BaseFragment<V : BaseContract.View, P : BaseContract.Presenter<V>> : RxFragment(), HasSupportFragmentInjector, BaseContract.View {
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    val TAG = javaClass.simpleName
    var mActivity: BaseDaggerActivity<*, *>? = null
    private var mUnBinder: Unbinder? = null
    private var mProgressDialog: ProgressDialog? = null

    @Inject
    lateinit var mPresenter: P

    /**
     * 获取Fragment的视图资源id
     *
     * @return fragment'ResLayoutId
     */
    @get:LayoutRes
    abstract val layoutId: Int

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        LogUtils.i(TAG, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.i(TAG, "onCreateView")
        val view = inflater.inflate(layoutId, container, false)
        mUnBinder = ButterKnife.bind(this, view)
        initView()
        return view
    }

    override fun onResume() {
        super.onResume()
        LogUtils.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.i(TAG, "onPause")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initData(savedInstanceState)
        super.onActivityCreated(savedInstanceState)
        LogUtils.i(TAG, "onActivityCreated")
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        //init mActivity
        if (context is BaseDaggerActivity<*, *>) {
            val activity = context as BaseDaggerActivity<*, *>?
            this.mActivity = activity
            activity!!.onFragmentAttached()
        }
        //Presenter attach the view
        mPresenter.onAttach(this as V)
        LogUtils.i(TAG, "onAttach")
    }


    override fun onDetach() {
        super.onDetach()
        mActivity!!.onFragmentDetached(TAG)
        mActivity = null
        if (mPresenter.isViewAttached) {
            mPresenter.onDetach()
        }
        LogUtils.i(TAG, "onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mUnBinder != null) {
            mUnBinder!!.unbind()
            mUnBinder = null
        }
        LogUtils.i(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.i(TAG, "onDestroy")
    }

    open fun onToolbarClick() {}

    override fun showLoading() {
        hideLoading()
        if (mActivity != null) {
            mProgressDialog = CommonUtils.showLoadingDialog(mActivity!!)
        }
    }

    override fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    override fun showMessage(@StringRes resId: Int) {
        if (mActivity != null) {
            mActivity!!.showMessage(resId)
        }
    }

    override fun showMessage(message: String) {
        if (mActivity != null) {
            mActivity!!.showMessage(message)
        }
    }

    override fun showError(@StringRes resId: Int) {
        if (mActivity != null) {
            mActivity!!.showError(resId)
        }
    }

    override fun showError(message: String) {
        if (mActivity != null) {
            mActivity!!.showError(message)
        }
    }

    /**
     * Fragment/Activity中方法,声明在view中;便于在mvp中的presenter里调用;
     *
     * @param <T> T
     * @return ObservableTransformer view层状态为STOP时调用RxLifeCycle来停止[DataManager]事物.
    </T> */
    override fun <T> bindUntilStopEvent(): LifecycleTransformer<T> {
        return bindUntilEvent(FragmentEvent.STOP)
    }


    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 初始化数据
     *
     * @param saveInstanceState Bundle
     */
    abstract fun initData(saveInstanceState: Bundle?)

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }
}
