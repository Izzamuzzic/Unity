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

package com.zwq65.unity.data.network.retrofit.response.enity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/08/30
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
@SuppressLint("ParcelCreator")
@Parcelize
open
data class Article(var _id: String?, var createdAt: String?, var desc: String?,var publishedAt: String?,
                   var source: String?, var type: String?, var url: String?,
                   var isUsed: Boolean = false, var who: String?, var images: List<String>?, var image: Image?) : Parcelable {
    /**
     * _id : 59a4ea09421aa901b9dc4652
     * createdAt : 2017-08-29T12:14:01.783Z
     * desc : 在任何非 MIUI 设备上体验小米系统级推送。
     * images : ["http://img.gank.io/1c974dca-f68d-4925-826e-863ac8a40d48"]
     * publishedAt : 2017-08-29T12:19:18.634Z
     * source : chrome
     * type : Android
     * url : https://github.com/Trumeet/MiPushFramework
     * used : true
     * who : 代码家
     */
}
