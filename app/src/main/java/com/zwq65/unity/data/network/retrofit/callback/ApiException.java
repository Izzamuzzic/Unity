package com.zwq65.unity.data.network.retrofit.callback;

/**
 * ================================================
 * api返回数据异常
 * <p>
 * Created by NIRVANA on 2018/02/05.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class ApiException extends Exception {
    public String code;
    public String message;


    public ApiException(Throwable throwable, String code) {
        super(throwable);
        this.code = code;
    }


}
