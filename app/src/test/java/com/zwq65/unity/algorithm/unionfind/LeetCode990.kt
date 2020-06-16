package com.zwq65.unity.algorithm.unionfind

import org.junit.Test

/**
 * ================================================
 * <p>
 * <a href="https://leetcode-cn.com/problems/satisfiability-of-equality-equations/">990. 等式方程的可满足性</a>.
 * Created by NIRVANA on 2019/7/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LeetCode990 {
    @Test
    fun test() {
        val array = arrayOf("a==b", "b==c", "a==c", "c!=b")
        val answer = equationsPossible(array)

        print("answer:$answer")
    }

    private fun equationsPossible(equations: Array<String>): Boolean {

        var uf = UF(26)
        for (str in equations) {
            if (str[1] == '=') {
                uf.union(str[0] - 'a', str[3] - 'a')
            }
        }

        for (str in equations) {
            if (str[1] == '!' && uf.find(str[0] - 'a') == uf.find(str[3] - 'a')) {
                return false
            }
        }

        return true
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
