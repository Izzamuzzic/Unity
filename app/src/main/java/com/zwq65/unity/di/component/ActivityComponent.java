/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.zwq65.unity.di.component;


import com.zwq65.unity.di.PerActivity;
import com.zwq65.unity.di.module.ActivityModule;
import com.zwq65.unity.ui.album.AlbumFragment;
import com.zwq65.unity.ui.album.image_detail.ImageActivity;
import com.zwq65.unity.ui.login.LoginActivity;
import com.zwq65.unity.ui.main.MainActivity;
import com.zwq65.unity.ui.personal_center.PersonalCenterActivity;
import com.zwq65.unity.ui.personal_center.tabs.tab_collection.TabCollectionFragment;

import dagger.Component;

/**
 * Created by janisharali on 27/01/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(LoginActivity activity);

    void inject(ImageActivity activity);

    void inject(AlbumFragment fragment);

    void inject(PersonalCenterActivity activity);

    void inject(TabCollectionFragment fragment);
}
