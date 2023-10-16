package com.yds.featured

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val res  = "20211202"
        println(res.contains(Regex("[^a-z]"))||res.contains(Regex("[^0-9]")))
//        val a: Int = 100
//        val boxedA: Int? = a
//        val anotherBoxedA: Int? = a
//
//        val b: Int = 10000
//        val boxedB: Int? = b
//        val anotherBoxedB: Int? = b
//
//        val d: Int = 10000
//        val boxedD: Int? = d
//        val anotherBoxedD: Int? = d

//        val c:Int? = 10000
//        val anotherC:Int? = c
////
//
//
//        println(boxedD === anotherBoxedD)
//        println(boxedA === anotherBoxedA) // true
//        println(boxedB === anotherBoxedB) // false
//        println(c === anotherC)
    }
}