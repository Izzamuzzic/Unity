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
open class Video(var _id: String?, var createdAt: String?, var desc: String?, var publishedAt: String?,
                 var source: String?, var type: String?, var url: String?,
                 var isUsed: Boolean = false, var who: String?, var image: Image?) : Parcelable {
    /**
     * _id : 59906c33421aa90f4919c7e1
     * createdAt : 2017-08-13T23:11:47.518Z
     * desc : DC最快的闪电侠，在你看标题时就已跑到银河尽头
     * publishedAt : 2017-08-14T17:04:50.266Z
     * source : chrome
     * type : 休息视频
     * url : http://www.bilibili.com/video/av13207512/
     * used : true
     * who : LHF
     */
}
