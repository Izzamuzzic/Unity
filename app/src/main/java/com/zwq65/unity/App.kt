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

package com.zwq65.unity

import android.content.Context
import android.support.multidex.MultiDex
import android.widget.Toast

import com.blankj.utilcode.util.Utils
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.crashreport.CrashReport
import com.zwq65.unity.di.component.DaggerApplicationComponent
import com.zwq65.unity.utils.ToastUtils

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * ================================================
 * unity Application
 * <p>
 * Created by NIRVANA on 2017/06/28.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Utils.init(this)
        initBugly()
        initStetho()
        initLeakCanary()
    }

    /**
     * return an [AndroidInjector] for the concrete [ ]. Typically, that injector is a [dagger.Component].
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    /**
     * 初始化bugly
     */
    private fun initBugly() {
        CrashReport.initCrashReport(applicationContext, "d60b53237b", false)
    }

    /**
     * 初始化Stetho(可以在chrome中方便地查看app数据库等信息,release版本关闭)
     */
    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }

    /**
     * 初始化LeakCanary(内存泄漏检测工具)
     */
    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }

    fun showToast(msg: String) {
        ToastUtils.makeText(msg, Toast.LENGTH_SHORT)
    }

    companion object {

        var instance: App? = null
            private set
    }
}