package com.zwq65.unity.algorithm.sort

import org.junit.Test

/**
 *================================================
 * 冒泡排序
 *
 * 冒泡排序（Bubble Sort，台湾译为：泡沫排序或气泡排序）是一种简单的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果他们的顺序错误就把他们交换过来。
 * 走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因为越大的元素会经由交换慢慢“浮”到数列的顶端，故名。
 * 冒泡排序虽然简单但是对于 n 数量级很大的时候，其实是很低效率的。所以实际生产中很少使用这种排序算法。下面我们看下这种算法的具体实现思路：
 *
 * 冒泡排序总结：
 * 时间平均复杂度为O(n²)。
 * 空间复杂度为 O(1)。
 * 冒泡排序为稳定排序。
 *
 * Created by NIRVANA on 2018/3/2
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
class BubbleSortTest {
    companion object {
        val array = arrayOf(11, 23, 3, 545, 342, 6564, 7)
    }

    @Test
    fun sort() {
        for (i in 0..array.size) {
            for (j in 1 until array.size - i) {

                if (array[j - 1] > array[j]) {
                    val temp = array[j]
                    array[j] = array[j - 1]
                    array[j - 1] = temp
                }
            }
        }

        for (item in array) {
            println(item)
        }
    }
}