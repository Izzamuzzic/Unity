package com.zwq65.unity.algorithm.unionfind

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
        val array = arrayOf("/\\", "\\/")
        val number = regionsBySlashes(array)

        print("number:$number")
    }

    private fun regionsBySlashes(grid: Array<String>): Int {
        val size = grid.size
        val uf = UF(4 * size * size)
        for (i in 0 until size) {
            for (j in 0 until size) {
                val root = 4 * (size * i + j)
                val charr = grid[i][j]

                if (charr != '/') {
                    uf.union(root + 1, root + 0)
                    uf.union(root + 3, root + 2)
                }
                if (charr != '\\') {
                    uf.union(root + 1, root + 2)
                    uf.union(root + 3, root + 0)
                }
                if (i != size - 1) {
                    // 如果不是最后一行，则向下归并
                    uf.union(root + 2, (root + 4 * size) + 0)
                }
                if (j != size - 1) {
                    // 如果不是最后一列，则向右归并
                    uf.union(root + 1, (root + 4) + 3)
                }
            }
        }

        var answer = 0
        for (i in 0 until 4 * size * size) {
            if (i == uf.find(i)) {
                answer++
            }
        }

        return answer
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
