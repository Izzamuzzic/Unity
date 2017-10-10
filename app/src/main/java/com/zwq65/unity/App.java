package com.zwq65.unity;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.zwq65.unity.di.component.ApplicationComponent;
import com.zwq65.unity.di.component.DaggerApplicationComponent;
import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.ToastUtils;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by zwq65 on 2017/06/28.
 * Unity
 */

public class App extends DaggerApplication {

    private static App unityApp;

    public static App getInstance() {
        return unityApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        unityApp = this;
        initBugly();
        initStetho();
        initLeakcanary();
    }

    /**
     * return an {@link AndroidInjector} for the concrete {@link
     * DaggerApplication}. Typically, that injector is a {@link dagger.Component}.
     */
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ApplicationComponent appComponent = DaggerApplicationComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "d60b53237b", false);
    }

    /**
     * 初始化Stetho(可以在chrome中方便地查看app数据库等信息,release版本关闭)
     */
    private void initStetho() {
        //仅在debug版本开启
        if (CommonUtils.isApkInDebug(this)) {
            Stetho.initializeWithDefaults(this);
        }
    }

    /**
     * 初始化Leakcanary(内存泄漏检测工具)
     */
    private void initLeakcanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static void showShortToast(String msg) {
        ToastUtils.makeText(msg, Toast.LENGTH_SHORT);
    }
}

