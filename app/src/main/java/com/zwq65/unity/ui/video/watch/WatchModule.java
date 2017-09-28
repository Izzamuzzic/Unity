package com.zwq65.unity.ui.video.watch;


import com.zwq65.unity.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/28
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Module
public abstract class WatchModule {
    @ActivityScoped
    @Binds
    abstract WatchContract.Presenter<WatchContract.View> providerWatchPresenter(WatchPresenter<WatchContract.View> watchPresenter);
}