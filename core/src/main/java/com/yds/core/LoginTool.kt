package com.yds.core

import java.util.*

object LoginTool {

    private val loader by lazy {
        ServiceLoader.load(ILogin::class.java)
    }

    fun logout(){
        val iterator = loader.iterator()
        while (iterator.hasNext()){
            iterator.next().logout()
        }
    }

}