package com.zwq65.unity.kotlin

import org.junit.Test
import kotlin.properties.Delegates

/**
 *================================================
 *
 * Created by NIRVANA on 2018/6/27
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
class ObservableTest {

    @Test
    fun test() {
        //可观察属性:Kotlin通过 Delegates.observable()实现可观察属性.
        //name是一个属性，改变它的值都会自动回调{ kProperty,  oldName,  newName -> }这个lambda表达式。
        //简单来说，我们可以监听name这个属性的变化。
        var name: String by Delegates.observable("wang", onChange = { property, oldValue, newValue ->
            println("property:$property | oldValue:$oldValue | newValue:$newValue")
        })

        name = "zhang"
        name = "li"
    }
}