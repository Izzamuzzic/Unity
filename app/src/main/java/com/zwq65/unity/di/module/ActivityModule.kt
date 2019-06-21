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

package com.zwq65.unity.di.module

import com.zwq65.unity.di.ActivityScoped
import com.zwq65.unity.ui.activity.*
import com.zwq65.unity.ui.module.*
import com.zwq65.unity.ui.provider.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

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
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(MainModule::class), (AlbumProvider::class), (RestVideoProvider::class), (TestProvider::class), (ArticleProvider::class), (TabArticleProvider::class)])
    internal abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(AccountModule::class)])
    internal abstract fun accountActivity(): AccountActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(ImageModule::class)])
    internal abstract fun imageActivity(): ImageActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(LoginModule::class)])
    internal abstract fun loginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(WebArticleModule::class)])
    internal abstract fun webArticleActivity(): WebArticleActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [(WatchModule::class)])
    internal abstract fun watchActivity(): WatchActivity

}
