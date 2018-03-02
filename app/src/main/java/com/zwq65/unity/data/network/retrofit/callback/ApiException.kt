package com.zwq65.unity.data.network.retrofit.callback

/**
 * ================================================
 * api返回数据异常[Exception]
 * <p>
 * Created by NIRVANA on 2018/02/05.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class ApiException(throwable: Throwable, var code: String) : Exception(throwable) {
    override var message: String = ""
}
