package com.zwq65.unity.ui.album;

import com.zwq65.unity.di.FragmentScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================ */
@Module
public abstract class AlbumProvider {
    @FragmentScoped
    @ContributesAndroidInjector(modules = AlbumModule.class)
    abstract AlbumFragment albumFragment();

}
