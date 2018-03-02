package com.zwq65.unity.data.network.retrofit.callback


import io.reactivex.Flowable
import io.reactivex.functions.Function

/**
 * ================================================
 * api返回数据异常统一处理方法类
 * <p>
 * Created by NIRVANA on 2018/02/05.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class HttpErrorFunction<T> : Function<Throwable, Flowable<T>> {
    /**
     * Applies this function to the given argument.
     *
     * @param throwable the function argument
     * @return the function result
     */
    override fun apply(throwable: Throwable): Flowable<T> {
        //ExceptionEngine为处理异常的驱动器
        return Flowable.error(ExceptionHandler.handleException(throwable))
    }
}
