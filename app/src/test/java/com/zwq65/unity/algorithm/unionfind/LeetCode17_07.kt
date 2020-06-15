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
class LeetCode17_07 {
    @Test
    fun test() {
        val names = arrayOf("John(15)", "Jon(12)", "Chris(13)", "Kris(4)", "Christopher(19)")
        val synonyms = arrayOf("(Jon,John)", "(John,Johnny)", "(Chris,Kris)", "(Chris,Christopher)")

        val answer = trulyMostPopular(names, synonyms)
        print("answer:$answer")
    }

    private fun trulyMostPopular(names: Array<String>, synonyms: Array<String>): Array<String> {
        val answer = mutableListOf<String>()

        var uf = UF(names.size)
        for(i in synonyms.indices){
            synonyms[i]
        }

        return answer.toTypedArray()
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
