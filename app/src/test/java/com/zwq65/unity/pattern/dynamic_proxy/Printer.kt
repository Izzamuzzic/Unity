package com.zwq65.unity.pattern.dynamic_proxy

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2017/11/15
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class Printer : IPrinter {

    override fun print() {
        println("打印文字")
    }

    override fun cancelPrint() {
        println("取消打印")
    }
}
