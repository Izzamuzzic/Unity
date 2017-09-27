package com.zwq65.unity.ui.test;

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
public abstract class TestModule {
    @FragmentScoped
    @Binds
    abstract TestContract.Presenter<TestContract.View> provideTestPresenter(
            TestPresenter<TestContract.View> testPresenter);
}
