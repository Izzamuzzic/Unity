package com.zwq65.unity.algorithm.sort

import org.junit.Test

/**
 *================================================
 * 快速排序
 *
 * 快速排序由C. A. R. Hoare在1962年提出。它的基本思想是：通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，
 * 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
 * 1.从第一个元素开始，该元素可以认为已经被排序
 * 2.取出下一个元素，在已经排序的元素序列中从后向前扫描
 * 3.如果该元素（已排序）大于新元素，将该元素移到下一位置
 * 4.重复步骤 3，直到找到已排序的元素小于或者等于新元素的位置
 * 5.将新元素插入到该位置后
 * 6.重复步骤 2~5
 *
 * 快速排序总结：
 * 对于快速排序的时间度取决于其递归的深度，如果递归深度又决定于每次关键值得取值所以在最好的情况下每次都取到数组中间值，那么此时算法时间复杂度最优为 O(nlogn)。当然最坏情况就是之前我们分析的有序数组，那么每次都需要进行 n 次比较则 时间复杂度为 O(n²)，但是在平均情况 时间复杂度为 O(nlogn)
 * 空间复杂度为 O(1)。
 * 快速排序为不稳定排序。
 *
 * Created by NIRVANA on 2018/3/2
 * Contact with <zwq651406441@gmail.com>
 *================================================
 */
class HeapSortTest {
    companion object {
        val array = arrayOf(7, 10, 3, 6, 4, 1, 7, 5)
    }

    @Test
    fun sort() {
        for (i in 0 until array.size) {
            //每次建堆就可以排除一个元素了
            maxHeapify(array, array.size - i)
            //交换
            val temp = array[0]
            array[0] = array[array.size - 1 - i]
            array[array.size - 1 - i] = temp
        }
        for (item in array) {
            println(item)
        }
    }

    /**
     * 完成一次建堆，最大值在堆的顶部(根节点)
     */
    fun maxHeapify(arrays: Array<Int>, size: Int) {
        // 从数组的尾部开始，直到第一个元素(角标为0)
        for (i in size - 1 downTo 0) {
            heapify(arrays, i, size)
        }
    }

    /**
     * 建堆
     *
     * @param arrays          看作是完全二叉树
     * @param currentRootNode 当前父节点位置
     * @param size            节点总数
     */
    fun heapify(arrays: Array<Int>, currentRootNode: Int, size: Int) {
        if (currentRootNode < size) {
            //左子树和右字数的位置
            val left = 2 * currentRootNode + 1
            val right = 2 * currentRootNode + 2

            //把当前父节点位置看成是最大的
            var max = currentRootNode

            if (left < size) {
                //如果比当前根元素要大，记录它的位置
                if (arrays[max] < arrays[left]) {
                    max = left
                }
            }
            if (right < size) {
                //如果比当前根元素要大，记录它的位置
                if (arrays[max] < arrays[right]) {
                    max = right
                }
            }
            //如果最大的不是根元素位置，那么就交换
            if (max != currentRootNode) {
                val temp = arrays[max]
                arrays[max] = arrays[currentRootNode]
                arrays[currentRootNode] = temp

                //继续比较，直到完成一次建堆
                heapify(arrays, max, arrays.size)
            }
        }
    }
}