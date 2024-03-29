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

package com.zwq65.unity.ui.module

import com.zwq65.unity.data.network.retrofit.response.enity.Article
import com.zwq65.unity.ui.contract.TabArticleContract
import com.zwq65.unity.ui.presenter.TabArticlePresenter

import dagger.Binds
import dagger.Module

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Module
abstract class TabArticleModule {
    /**
     * 不使用注解[@FragmentScoped][com.zwq65.unity.di.FragmentScoped],因为该presenter作用于多个fragment，不可唯一。
     *
     * @param tabArticlePresenter tabArticlePresenter
     * @return tabArticlePresenter
     */
    @Binds
    internal abstract fun provideTabArticlePresenter(
            tabArticlePresenter: TabArticlePresenter<TabArticleContract.View<Article>>): TabArticleContract.Presenter<TabArticleContract.View<Article>>
}
