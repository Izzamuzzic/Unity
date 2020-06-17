package com.zwq65.unity.algorithm.dataStructures

/**
 * ================================================
 *
 * Created by NIRVANA on 2020/6/17.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LinkedQueueOfStrings {
    private var first: Node? = null
    private var last: Node? = null

    val isEmpty: Boolean
        get() = first == null

    fun enqueue(item: String) {
        val oldlast = last
        last = Node()
        last?.item = item
        last?.next = null
        if (isEmpty) first = last else oldlast?.next = last
    }

    fun dequeue(): String? {
        val item: String? = first?.item
        first = first?.next
        if (isEmpty) last = null
        return item
    }

    private inner class Node {
        var item: String? = null
        var next: Node? = null
    }

}