package com.zwq65.unity.kotlin

import com.zwq65.unity.kotlin.objectOriented.MathTeacher
import org.junit.Test

/**
 *================================================
 *
 * Created by NIRVANA on 2018/3/1
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
class KotlinTest {
    private var a: Int = 1
    private val s = "a is $a"

    /**
     * 使用下划线增加数值常量的可读性
     */
    private val oneMillion = 1_000_000

    @Test
    fun test() {
//        //单行注释
//        /**
//         * 块注释
//         */
//        a = 2
////        println("${s.replace("is", "was")},but now is $a")
////        val asc = Array(5, { i -> (i * i).toDouble() })
//        val text = """
//    |Tell me and I forget.
//    |Teach me and I remember.
//    |Involve me and I learn.
//    |(Benjamin Franklin)
//    """.trimMargin(">")
//
//        val price = """${'$'}9.99"""
//        println(price)

//        val person = getPerson()
//        println("name:${person.name},phone:${person.phone}")

        var m  = MathTeacher("old name")
        m.teach("体育")

    }

    private fun getPerson(): Person {
        return Person().apply {
            name = "赵文强"
            phone = 18758248004
        }
    }
}