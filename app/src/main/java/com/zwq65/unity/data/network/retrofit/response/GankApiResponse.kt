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

package com.zwq65.unity.data.network.retrofit.response

import com.google.gson.annotations.SerializedName

/**
 * ================================================
 * gank io返回数据基类
 * 变量使用[SerializedName]注解标记,混淆后gson可根据[SerializedName]的字符来解析json,否则解析失败!
 *
 * Created by NIRVANA on 2017/05/03.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class GankApiResponse<T> {
    @SerializedName("error")
    var isError: Boolean = false

    @SerializedName("results")
    var data: T? = null

}
