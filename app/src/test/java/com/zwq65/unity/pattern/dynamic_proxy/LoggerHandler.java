package com.zwq65.unity.pattern.dynamic_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/11/15
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class LoggerHandler implements InvocationHandler {

    private Object target;

    public LoggerHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("準備打印");
        Object response = method.invoke(target, args);
        System.out.println("結束打印");
        return response;
    }
}
