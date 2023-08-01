package com.yds.eyepetizer

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    /**
     * 给你一个非负整数 x ，计算并返回 x 的 算术平方根 。

    由于返回类型是整数，结果只保留 整数部分 ，小数部分将被 舍去 。

    注意：不允许使用任何内置指数函数和算符，例如 pow(x, 0.5) 或者 x ** 0.5 。
     */
    @Test
    fun addition_isCorrect() {
        val arr = intArrayOf(1, 2, 0, 0, 0, 3, 0, 4, 0, 0)
        moveZeroes(arr)
        println(arr.asList().toString())
    }

    // 1 2 0 0 0 3 0 4 0 0
    fun moveZeroes(nums: IntArray): Unit {

    }
}