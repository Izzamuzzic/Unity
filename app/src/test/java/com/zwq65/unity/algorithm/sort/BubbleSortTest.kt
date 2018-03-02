package com.zwq65.unity.algorithm.sort

import org.junit.Test

/**
 *================================================
 *冒泡排序
 *
 * 冒泡排序恐怕是我们计算机专业课程上以第一个接触到的排序算法，也算是一种入门级的排序算法。
 * 冒泡排序虽然简单但是对于 n 数量级很大的时候，其实是很低效率的。所以实际生产中很少使用这种排序算法。下面我们看下这种算法的具体实现思路：
 * ###冒泡排序算法原理：
 * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
 * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。这步做完后，最后的元素会是最大的数。
 * 针对所有的元素重复以上的步骤，除了最后一个。
 * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
 *
 * 冒泡排序总结：
 * 冒泡排序的算法时间平均复杂度为O(n²)。
 * 空间复杂度为 O(1)。
 * 冒泡排序为稳定排序。
 * 链接：https://juejin.im/post/5a96d6b15188255efc5f8bbd
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