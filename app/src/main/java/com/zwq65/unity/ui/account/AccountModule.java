package com.zwq65.unity.ui.account;

import com.zwq65.unity.di.ActivityScoped;

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
public abstract class AccountModule {
    @ActivityScoped
    @Binds
    abstract AccountContract.Presenter<AccountContract.View> provideAccountPresenter(
            AccountPresenter<AccountContract.View> accountPresenter);
}
