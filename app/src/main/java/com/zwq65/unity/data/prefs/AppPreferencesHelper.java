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

package com.zwq65.unity.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.zwq65.unity.di.ApplicationContext;
import com.zwq65.unity.di.PreferenceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by janisharali on 27/01/17.
 */

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_DAY_NIGHT_MODE = "PREF_KEY_DAY_NIGHT_MODE";

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public Boolean getDayNightmode() {
        return mPrefs.getBoolean(PREF_KEY_DAY_NIGHT_MODE, false);
    }

    @Override
    public void setDayNightmode(Boolean isNightmode) {
        mPrefs.edit().putBoolean(PREF_KEY_DAY_NIGHT_MODE, isNightmode).apply();
    }
}
