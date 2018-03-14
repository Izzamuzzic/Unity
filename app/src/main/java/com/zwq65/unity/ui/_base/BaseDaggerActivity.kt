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
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.gyf.barlibrary.ImmersionBar
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.zwq65.unity.R
import com.zwq65.unity.data.DataManager
import com.zwq65.unity.utils.CommonUtils
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * ================================================
 * Mvp架构下集合Dagger2库的activity基类
 * An [AppCompatActivity] that injects its members in [.onCreate] and can be
 * used to inject `Fragment`s attached to it.
 *
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
abstract class BaseDaggerActivity<in V : BaseContract.View, P : BaseContract.Presenter<V>> : BaseActivity(), HasFragmentInjector, HasSupportFragmentInjector, BaseContract.View, BaseFragment.Callback {
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    var toolbar: Toolbar? = null
        internal set
    private var mProgressDialog: ProgressDialog? = null
    @Inject
    lateinit var mPresenter: P

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return supportFragmentInjector
    }

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment>? {
        return frameworkFragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mPresenter.onAttach(this as V)
        if (initBaseTooBar() != null && initBaseTooBar()!!) {
            setupBaseToolbar()
        } else {
            //不含toolbar的activity，采用fitsSystemWindows(false)方式实现沉浸栏
            ImmersionBar.with(this).fitsSystemWindows(false).init()
        }
        if (intent != null) {
            dealIntent(intent)
        }
        initView()
        initData()
    }

    /**
     * 是否加载默认ToolBar
     *
     * @return Boolean
     */
    abstract fun initBaseTooBar(): Boolean?

    /**
     * Intent不为空的话，处理之
     *
     * @param intent 获取到的Intent
     */
    abstract fun dealIntent(intent: Intent)

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 初始化数据
     */
    abstract fun initData()

    protected fun setupBaseToolbar() {
        toolbar = findViewById(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            toolbar!!.setNavigationIcon(R.mipmap.ic_menu_white)
            //含toolbar的activity，实现沉浸式状态栏
            ImmersionBar.with(this).titleBar(toolbar!!).init()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy()
        //解绑presenter
        if (mPresenter.isViewAttached) {
            mPresenter.onDetach()
        }
    }

    override fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtils.showLoadingDialog(this)
    }

    override fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    override fun showMessage(@StringRes resId: Int) {
        showMessage(getString(resId))
    }

    override fun showMessage(message: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        val textView = sbView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    override fun showError(@StringRes resId: Int) {
        showError(getString(resId))
    }

    override fun showError(message: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.warn_color))
        val textView = sbView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    /**
     * Fragment/Activity中方法,声明在view中;便于在mvp中的presenter里调用;
     *
     * @return ObservableTransformer view层状态为STOP时调用RxLifeCycle来停止[DataManager]事物.
     */
    override fun <T> bindUntilStopEvent(): LifecycleTransformer<T> {
        return bindUntilEvent(ActivityEvent.STOP)
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

}
