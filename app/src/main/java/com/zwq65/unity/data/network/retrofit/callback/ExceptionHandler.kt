package com.zwq65.unity.data.network.retrofit.callback

import android.net.ParseException

import com.google.gson.JsonParseException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException

import org.json.JSONException

import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * ================================================
 * api交互异常处理统一返回[ApiException]
 *
 *
 * Created by NIRVANA on 2018/02/05.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
internal object ExceptionHandler {
    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504

    fun handleException(e: Throwable): ApiException {
        val ex: ApiException
        if (e is HttpException) {
            ex = ApiException(e, Error.HTTP_ERROR.toString())
            when (e.code()) {
                UNAUTHORIZED, FORBIDDEN, NOT_FOUND, REQUEST_TIMEOUT, GATEWAY_TIMEOUT, INTERNAL_SERVER_ERROR, BAD_GATEWAY, SERVICE_UNAVAILABLE ->
                    //均视为网络错误
                    ex.message = "网络异常"
                else -> ex.message = "网络异常"
            }
            return ex
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {
            ex = ApiException(e, Error.PARSE_ERROR.toString())
            //均视为解析错误
            ex.message = "解析异常"
            return ex
        } else if (e is ConnectException) {
            ex = ApiException(e, Error.NETWORK_ERROR.toString())
            ex.message = "网络异常：连接失败"
            return ex
        } else if (e is SocketTimeoutException) {
            ex = ApiException(e, Error.NETWORK_ERROR.toString())
            ex.message = "网络异常：连接超时"
            return ex
        } else if (e is ServerException) {
            //服务器返回的错误
            ex = ApiException(e, e.code)
            ex.message = e.message
            return ex
        } else {
            ex = ApiException(e, Error.UNKNOWN.toString())
            //未知错误
            ex.message = "未知异常"
            return ex
        }
    }

    object Error {
        /**
         * 未知错误
         */
        internal const val UNKNOWN = 1000
        /**
         * 解析错误
         */
        internal const val PARSE_ERROR = 1001
        /**
         * 网络错误
         */
        internal const val NETWORK_ERROR = 1002
        /**
         * 协议出错
         */
        internal const val HTTP_ERROR = 1003
    }
}
