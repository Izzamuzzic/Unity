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

package com.zwq65.unity.ui.provider;

import com.zwq65.unity.di.FragmentScoped;
import com.zwq65.unity.ui.fragment.RestVideoFragment;
import com.zwq65.unity.ui.module.RestVideoModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/09/27
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Module
public abstract class RestVideoProvider {
    @FragmentScoped
    @ContributesAndroidInjector(modules = RestVideoModule.class)
    abstract RestVideoFragment restVideoFragment();
}