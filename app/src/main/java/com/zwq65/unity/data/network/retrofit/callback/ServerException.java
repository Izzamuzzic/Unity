package com.zwq65.unity.data.network.retrofit.callback;

/**
 * ================================================
 * 服务器返回数据异常
 * <p>
 * Created by NIRVANA on 2018/02/05.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class ServerException extends RuntimeException {
    public String code;
    public String message;

    public ServerException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

}
