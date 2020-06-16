package com.zwq65.unity.algorithm.unionfind

import org.junit.Test

/**
 * ================================================
 * <p>
 * <a href="https://leetcode-cn.com/problems/most-stones-removed-with-same-row-or-column/">947. 移除最多的同行或同列石头</a>.
 * Created by NIRVANA on 2019/7/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LeetCode947 {
    @Test
    fun test() {
        val array = arrayOf(intArrayOf(0, 0), intArrayOf(0, 2),
                intArrayOf(1, 1),  intArrayOf(2, 0), intArrayOf(2, 2))
        val number = removeStones(array)

        print("number:$number")
    }

    private fun removeStones(stones: Array<IntArray>): Int {
        var N = stones.size
        var dsu = UF(20000)

        for (stone in stones)
            dsu.union(stone[0], stone[1] + 10000)

        var seen = HashSet<Int>()
        for (stone in stones)
            seen.add(dsu.find(stone[0]))

        return N - seen.size
    }

    class UF(size: Int) {
        private var parent = IntArray(size)

        init {
            //初始化:parent指向本身
            for (i in 0 until size) {
                parent[i] = i
            }
        }

        fun find(x: Int): Int {
            return if (x == parent[x]) {
                x
            } else {
                parent[x] = find(parent[x])
                find(parent[x])
            }

        }

        fun union(x: Int, y: Int) {
            parent[find(x)] = parent[find(y)]
        }

    }

}
