/*
 *    Copyright [2017] [NIRVANA PRIVATE LIMITED]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zwq65.unity.di.module;

import com.zwq65.unity.di.ActivityScoped;
import com.zwq65.unity.ui.activity.AccountActivity;
import com.zwq65.unity.ui.module.AccountModule;
import com.zwq65.unity.ui.provider.AlbumProvider;
import com.zwq65.unity.ui.activity.ImageActivity;
import com.zwq65.unity.ui.module.ImageModule;
import com.zwq65.unity.ui.provider.ArticleProvider;
import com.zwq65.unity.ui.provider.TabArticleProvider;
import com.zwq65.unity.ui.activity.WebArticleActivity;
import com.zwq65.unity.ui.module.WebArticleModule;
import com.zwq65.unity.ui.activity.LoginActivity;
import com.zwq65.unity.ui.module.LoginModule;
import com.zwq65.unity.ui.activity.MainActivity;
import com.zwq65.unity.ui.module.MainModule;
import com.zwq65.unity.ui.provider.TestProvider;
import com.zwq65.unity.ui.provider.RestVideoProvider;
import com.zwq65.unity.ui.activity.WatchActivity;
import com.zwq65.unity.ui.module.WatchModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent
 * that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules
 * and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = {MainModule.class, AlbumProvider.class, RestVideoProvider.class,
            TestProvider.class, ArticleProvider.class, TabArticleProvider.class})
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AccountModule.class)
    abstract AccountActivity accountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ImageModule.class)
    abstract ImageActivity imageActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = WebArticleModule.class)
    abstract WebArticleActivity webArticleActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = WatchModule.class)
    abstract WatchActivity watchActivity();

}
