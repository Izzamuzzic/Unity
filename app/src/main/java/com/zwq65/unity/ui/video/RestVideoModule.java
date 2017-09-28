package com.zwq65.unity.ui.video;

import com.zwq65.unity.data.network.retrofit.response.enity.Video;
import com.zwq65.unity.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Module
public abstract class RestVideoModule {
    @FragmentScoped
    @Binds
    abstract RestVideoContract.Presenter<RestVideoContract.View<Video>> provideRestVidePresenter(
            RestVideoPresenter<RestVideoContract.View<Video>> restVideoPresenter);
}
