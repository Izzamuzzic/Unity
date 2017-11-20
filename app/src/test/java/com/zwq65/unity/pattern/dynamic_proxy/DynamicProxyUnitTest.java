package com.zwq65.unity.pattern.dynamic_proxy;

import java.lang.reflect.Proxy;

/**
 * ================================================
 * java动态代理
 * <p>
 * Created by NIRVANA on 2017/11/15
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class DynamicProxyUnitTest {

    @org.junit.Test
    public void test() {
        IPrinter printer = new Printer();
        LoggerHandler loggerHandler = new LoggerHandler(printer);
        IPrinter proxy = (IPrinter) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                printer.getClass().getInterfaces(), loggerHandler);
        proxy.print();
        proxy.cancelPrint();
    }

}
