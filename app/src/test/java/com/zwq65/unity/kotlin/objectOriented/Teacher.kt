package com.zwq65.unity.kotlin.objectOriented

/**
 *================================================
 *
 * Created by NIRVANA on 2018/3/13
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
interface Teacher {
    var name: String

    fun teach(subject: String) {
        println(subject)
    }

}