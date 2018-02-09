package com.zwq65.unity.data.network.retrofit.callback;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * ================================================
 * api交互异常处理统一返回{@link ApiException}
 * <p>
 * Created by NIRVANA on 2018/02/05.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class ExceptionHandler {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, String.valueOf(Error.HTTP_ERROR));
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    //均视为网络错误
                    ex.message = "网络异常";
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, String.valueOf(Error.PARSE_ERROR));
            //均视为解析错误
            ex.message = "解析异常";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, String.valueOf(Error.NETWORK_ERROR));
            ex.message = "网络异常：连接失败";
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, String.valueOf(Error.NETWORK_ERROR));
            ex.message = "网络异常：连接超时";
            return ex;
        } else if (e instanceof ServerException) {
            //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else {
            ex = new ApiException(e, String.valueOf(Error.UNKNOWN));
            //未知错误
            ex.message = "未知异常";
            return ex;
        }
    }

    public static class Error {
        /**
         * 未知错误
         */
        static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        static final int HTTP_ERROR = 1003;
    }
}
