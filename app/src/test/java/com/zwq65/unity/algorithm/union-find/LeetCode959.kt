package com.zwq65.unity.algorithm.leetcode

import org.junit.Test

/**
 * ================================================
 * <p>
 * <a href="https://leetcode-cn.com/problems/regions-cut-by-slashes">959. 由斜杠划分区域</a>.
 * Created by NIRVANA on 2019/7/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LeetCode959 {
    @Test
    fun test() {
        val array = arrayOf(" /\\", "///", "\\  ")
        for (s in array) {
            println(s)
        }
        val number = regionsBySlashes(array)

        print("number:$number")
    }

    private fun regionsBySlashes(grid: Array<String>): Int {

        return 0
    }

    class UF(size: Int) {
        private var parent: Array<Int> = arrayOf(size)

        init {
            //初始化:parent指向本身
            for (i in 0 until size) {
                parent[i] = i
            }
        }

        private fun find(x: Int): Int {
            return if (x == parent[x]) {
                x
            } else {
                parent[x] = parent[parent[x]]
                find(parent[x])
            }

        }

        fun union(x: Int, y: Int) {

        }

    }

}
