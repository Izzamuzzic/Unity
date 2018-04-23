package com.zwq65.unity.data.network.retrofit.callback


import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * ================================================
 * api返回数据异常统一处理方法类
 * <p>
 * Created by NIRVANA on 2018/02/05.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class HttpErrorFunction<T> : Function<Throwable, ObservableSource<T>> {
    /**
     * Applies this function to the given argument.
     *
     * @param throwable the function argument
     * @return the function result
     */
    override fun apply(throwable: Throwable): ObservableSource<T> {
        //ExceptionEngine为处理异常的驱动器
        return Observable.error(ExceptionHandler.handleException(throwable))
    }
}
