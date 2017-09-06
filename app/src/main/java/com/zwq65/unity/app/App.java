package com.zwq65.unity.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.tencent.bugly.crashreport.CrashReport;
import com.zwq65.unity.di.component.ApplicationComponent;
import com.zwq65.unity.di.component.DaggerApplicationComponent;
import com.zwq65.unity.di.module.ApplicationModule;
import com.zwq65.unity.utils.CommonUtils;
import com.zwq65.unity.utils.ToastUtils;

/**
 * Created by zwq65 on 2017/06/28.
 * Unity
 */

public class App extends MultiDexApplication {

    private ApplicationComponent mApplicationComponent;
    private static App unityApp;

    public static App getInstance() {
        return unityApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        unityApp = this;
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);
        initBugly();
        initStetho();
        initLeakcanary();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        MultiDex.install(newBase);
        super.attachBaseContext(newBase);
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

    private void initLeakcanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public static void showShortToast(String msg) {
        ToastUtils.makeText(msg, Toast.LENGTH_SHORT);
    }
}

