package com.zwq65.unity.ui.album;

import com.zwq65.unity.data.network.retrofit.response.enity.Image;
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
public abstract class AlbumModule {
    @FragmentScoped
    @Binds
    abstract AlbumContract.Presenter<AlbumContract.View<Image>> provideAlbumPresenter(
            AlbumPresenter<AlbumContract.View<Image>> albumPresenter);
}
