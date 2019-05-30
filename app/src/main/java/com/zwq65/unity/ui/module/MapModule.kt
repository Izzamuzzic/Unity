package com.zwq65.unity.ui.module


import com.zwq65.unity.di.ActivityScoped
import com.zwq65.unity.ui.contract.MapContract
import com.zwq65.unity.ui.presenter.MapPresenter

import dagger.Binds
import dagger.Module


/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2019/5/29
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Module
abstract class MapModule {
    @ActivityScoped
    @Binds
    internal abstract fun providerMapPresenter(
            mapPresenter: MapPresenter<MapContract.View>): MapContract.Presenter<MapContract.View>
}