package com.zwq65.unity.algorithm.sort

import org.junit.Test

/**
 *================================================
 * 选择排序
 *
 * 选择排序(Selection sort)是一种简单直观的排序算法。它的工作原理是每一次从待排序的数据元素中选出最小(或最大)的一个元素，存放在序列的起始(末尾)位置，
 * 直到全部待排序的数据元素排完。选择排序是不稳定的排序方法（比如序列[5， 5， 3]第一次就将第一个[5]与[3]交换，导致第一个5挪动到第二个5后面）。
 *
 * 判断某排序算法是否稳定，我们可以简单理解成：排序前2个相等的数其在序列的前后位置顺序和排序后它们两个的前后位置顺序相同
 * 1.如果相同，则是稳定的排序方法。
 *
 * 2.如果不相同，则是不稳定的排序方法
 *
 * 如果排序前的数组是[3,3,1]，假定我们使用选择排序的话，那第一趟排序后结果就是[1,3,3]。这个数组有两个相同的值，它俩在array[0]和array[1]，结果经过排序，array[0]的跑到了array[2]上了。
 * 那么这就导致：2个相等的数其在序列的前后位置顺序和排序后它们两个的前后位置顺序不相同，因此，我们就说它是不稳定的
 *
 * 选择排序总结：
 * 时间平均复杂度为O(n²)。
 * 空间复杂度为 O(1)。
 * 选择排序为不稳定排序。
 *
 * Created by NIRVANA on 2018/3/2
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
class SelectionSortTest {
    companion object {
        val array = arrayOf(11, 23, 3, 545, 342, 6564, 7, 5)
    }

    private var temp: Int = 0
    private var position: Int = 0

    @Test
    fun sort() {
        for (i in 0 until array.size - 1) {
            position = 0
            for (j in 0 until array.size - i) {
                if (array[position] < array[j]) {
                    position = j
                }
            }
            temp = array[position]
            array[position] = array[array.size - 1 - i]
            array[array.size - 1 - i] = temp
        }

        for (item in array) {
            println(item)
        }
    }
}