package com.zwq65.unity.ui.setting;


import com.zwq65.unity.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/10/10
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Module
public abstract class SettingModule {
    @ActivityScoped
    @Binds
    abstract SettingContract.Presenter<SettingContract.View>
    providerSettingPresenter(SettingPresenter<SettingContract.View>
                                     settingPresenter);
}