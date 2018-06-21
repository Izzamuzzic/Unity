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
open class Image(var _id: String?, var createdAt: String?, var desc: String?, var publishedAt: String?,
                 var source: String?, var type: String?, var url: String?,
                 var isUsed: Boolean = false, var who: String?, var width: Int?, var height: Int?) : Parcelable {
    /**
     * _id : 596ea620421aa90c9203d3bc
     * createdAt : 2017-07-19T08:21:52.67Z
     * desc : 7-19
     * publishedAt : 2017-07-19T13:23:20.375Z
     * source : chrome
     * type : 福利
     * url : https://ws1.sinaimg.cn/large/610dc034ly1fhovjwwphfj20u00u04qp.jpg
     * used : true
     * who : 代码家
     */
}
