package com.zwq65.unity.algorithm.dataStructures

/**
 * ================================================
 *
 * Created by NIRVANA on 2020/6/17.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LinkedStackOfStrings {
    private var first: Node? = null


    val isEmpty: Boolean
        get() = first == null

    fun push(item: String?) {
        val oldfirst = first
        first = Node()
        first!!.item = item
        first!!.next = oldfirst
    }

    fun pop(): String? {
        val item = first!!.item
        first = first!!.next
        return item
    }

    private inner class Node {
        var item: String? = null
        var next: Node? = null
    }
}