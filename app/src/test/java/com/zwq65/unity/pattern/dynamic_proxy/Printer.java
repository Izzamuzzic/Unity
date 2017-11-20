package com.zwq65.unity.pattern.dynamic_proxy;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/11/15
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class Printer implements IPrinter {
    public Printer() {
    }

    @Override
    public void print() {
        System.out.println("打印文字");
    }

    @Override
    public void cancelPrint() {
        System.out.println("取消打印");
    }
}
