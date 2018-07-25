package com.zwq65.unity.pattern.dynamic_proxy

import org.junit.Test

import java.lang.reflect.Proxy

/**
 * ================================================
 * java动态代理
 * <p>
 * Created by NIRVANA on 2017/11/15
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class DynamicProxyUnitTest {

    @Test
    fun test() {
        val printer = Printer()
        val loggerHandler = LoggerHandler(printer)
        val proxy = Proxy.newProxyInstance(Thread.currentThread().contextClassLoader,
                printer.javaClass.interfaces, loggerHandler) as IPrinter
        proxy.print()
        proxy.cancelPrint()
    }

}
