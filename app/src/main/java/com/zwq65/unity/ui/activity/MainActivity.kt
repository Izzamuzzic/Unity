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

package com.zwq65.unity.ui.activity

import android.content.Intent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.jakewharton.rxbinding2.view.RxView
import com.zwq65.unity.ui._base.BaseDaggerActivity
import com.zwq65.unity.ui._base.BaseFragment
import com.zwq65.unity.ui.contract.MainContract
import com.zwq65.unity.ui.fragment.AlbumFragment
import com.zwq65.unity.ui.fragment.ArticleFragment
import com.zwq65.unity.ui.fragment.RestVideoFragment
import com.zwq65.unity.ui.fragment.TestFragment
import com.zwq65.unity.utils.loadCircle
import com.zwq65.unity.utils.setCustomDensity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_left.*
import java.util.concurrent.TimeUnit


/**
 * ================================================
 * 首页[BaseDaggerActivity]
 *
 * Created by NIRVANA on 2017/06/29.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class MainActivity : BaseDaggerActivity<MainContract.View, MainContract.Presenter<MainContract.View>>(), MainContract.View, View.OnClickListener {

    private var disposable: Disposable? = null

    override val layoutId: Int
        get() = com.zwq65.unity.R.layout.activity_main

    private var firstClick: Long = 0

    override fun initBaseTooBar(): Boolean {
        return true
    }

    override fun dealIntent(intent: Intent) {

    }

    override fun initView() {
        //屏幕适配方案
        setCustomDensity(this, application, 375.0f)
        //将drawerLayout、toolBar绑定
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, com.zwq65.unity.R.string.navigation_drawer_open, com.zwq65.unity.R.string.navigation_drawer_close)
        drawer_layout?.addDrawerListener(toggle)
        toggle.syncState()

        addToolbarDoubleClick()
        iv_avatar?.setOnClickListener(this)
        iv_avatar?.loadCircle(com.zwq65.unity.R.mipmap.ic_avatar)
        tv_account_name?.setOnClickListener(this)
        ll_welfare?.setOnClickListener(this)
        ll_news?.setOnClickListener(this)
        ll_video?.setOnClickListener(this)
        ll_setting?.setOnClickListener(this)
        ll_out?.setOnClickListener(this)

        //默认跳转
        gotoFragment(AlbumFragment())
    }

    override fun initData() {

    }

    override fun onBackPressed() {
        if (drawer_layout?.isDrawerOpen(GravityCompat.START)!!) {
            drawer_layout?.closeDrawer(GravityCompat.START)
        } else {
            //双击退出app
            if (System.currentTimeMillis() - firstClick > DELAY_TIME_FINISH) {
                firstClick = System.currentTimeMillis()
                showMessage(com.zwq65.unity.R.string.str_finish_if_press_again)
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onClick(v: View?) {
        drawer_layout?.closeDrawer(GravityCompat.START)
        when (v?.id) {
            com.zwq65.unity.R.id.iv_avatar, com.zwq65.unity.R.id.tv_account_name ->
                //个人中心
                openActivity(AccountActivity::class.java)
            com.zwq65.unity.R.id.ll_welfare ->
                //福利图集
                gotoFragment(AlbumFragment())
            com.zwq65.unity.R.id.ll_news ->
                //技术文章
                gotoFragment(ArticleFragment())
            com.zwq65.unity.R.id.ll_video ->
                //休息视频
                gotoFragment(RestVideoFragment())
            com.zwq65.unity.R.id.ll_setting ->
                gotoFragment(TestFragment())
            com.zwq65.unity.R.id.ll_out -> onBackPressed()
            else -> {
            }
        }
    }

    /**
     * 添加toolBar双击监听事件
     */
    private fun addToolbarDoubleClick() {
        //buffer: 定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值
        //判断500ms内，如果接受到2次的点击事件，则视为用户双击操作
        if (toolbar != null) {
            disposable = RxView.clicks(toolbar!!).buffer(500, TimeUnit.MILLISECONDS, 2).subscribe { objects ->
                val clickNum = 2
                if (objects.size == clickNum) {
                    val fragmentManager = supportFragmentManager
                    val fragmentList = fragmentManager.fragments
                    if (fragmentList.size > 0) {
                        fragmentList
                                .filter { it.isVisible }
                                .filterIsInstance<BaseFragment<*, *>>()
                                .forEach { it.onToolbarClick() }
                    }
                }
            }
        }
    }

    private fun gotoFragment(fragment: BaseFragment<*, *>) {
        switchFragment(com.zwq65.unity.R.id.fl_main, fragment, fragment.javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    companion object {

        /**
         * 退出app延时时间
         */
        const val DELAY_TIME_FINISH = 2000
    }
}
