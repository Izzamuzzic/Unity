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

package com.zwq65.unity.data.prefs

import android.content.Context
import android.content.SharedPreferences

import com.zwq65.unity.di.ApplicationContext
import com.zwq65.unity.di.PreferenceInfo

import javax.inject.Inject
import javax.inject.Singleton

/**
 * ================================================
 * SharedPreferences [android.content.SharedPreferences]读写类
 *
 * Created by NIRVANA on 2017/01/27.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@Singleton
class AppPreferencesHelper @Inject
constructor(@ApplicationContext context: Context,
            @PreferenceInfo prefFileName: String) : PreferencesHelper {

    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override var dayNightMode: Boolean?
        get() = mPrefs.getBoolean(PREF_KEY_DAY_NIGHT_MODE, false)
        set(isNightMode) = mPrefs.edit().putBoolean(PREF_KEY_DAY_NIGHT_MODE, isNightMode!!).apply()

    companion object {
        private const val PREF_KEY_DAY_NIGHT_MODE = "PREF_KEY_DAY_NIGHT_MODE"
    }
}
