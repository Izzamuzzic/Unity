package com.zwq65.unity.data.network.retrofit.callback

/**
 * ================================================
 * 服务器返回数据异常
 *
 * Created by NIRVANA on 2018/02/05.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class ServerException(var code: String, override var message: String) : RuntimeException()
