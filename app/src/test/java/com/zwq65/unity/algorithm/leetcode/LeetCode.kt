package com.zwq65.unity.algorithm.leetcode

import org.junit.Test

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2019/7/15.
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
class LeetCode {
    @Test
    fun test() {
        val numbers = intArrayOf(2, 0, 2, 1, 1, 0, 2, 2, 1, 0, 0)
        sortColors(numbers)
        for (i in numbers) {
            print("$i  ")
        }
    }

    private fun sortColors(numbers: IntArray) {
        var firstCurs = 0
        var cursor = 0
        var secondCurs = numbers.size - 1
        var temp: Int
        while (cursor <= secondCurs) {
            when {
                numbers[cursor] == 0 -> {
                    temp = numbers[cursor]
                    numbers[cursor] = numbers[firstCurs]
                    numbers[firstCurs++] = temp
                    cursor++
                }
                numbers[cursor] == 2 -> {
                    temp = numbers[cursor]
                    numbers[cursor] = numbers[secondCurs]
                    numbers[secondCurs--] = temp
                }
                else ->
                    cursor++
            }

        }
    }

}
