package com.zwq65.unity.pattern.dynamic_proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/11/15
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LoggerHandler(private val target: Any) : InvocationHandler {

    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {
        println("準備打印")
        val response = method.invoke(target, *args)
        println("結束打印")
        return response
    }
}
