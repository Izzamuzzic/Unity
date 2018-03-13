package com.zwq65.unity.kotlin.objectOriented

/**
 *================================================
 *
 * Created by NIRVANA on 2018/3/13
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
class MathTeacher(override var name: String) : Teacher {

    override fun teach(subject: String) {
        super.teach(subject)
        name = "james "
        print(name + subject)
    }

}