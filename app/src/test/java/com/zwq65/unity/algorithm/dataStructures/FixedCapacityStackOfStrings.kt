package com.zwq65.unity.algorithm.dataStructures

/**
 * ================================================
 *
 * Created by NIRVANA on 2020/6/17.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class FixedCapacityStackOfStrings(capacity: Int) {
    private val s: Array<String?> = arrayOfNulls(capacity)
    private var N = 0
    val isEmpty: Boolean
        get() = N == 0

    fun push(item: String?) {
        s[N++] = item
    }

    fun pop(): String? {
        return s[--N]
    }

}