package com.yds.core

import java.util.*

object UserInfoTool {

    private val loader by lazy {
        ServiceLoader.load(IUserInfo::class.java)
    }

    fun getUserName(): String? {
        val iterator = loader.iterator()
        while (iterator.hasNext()) {
            return iterator.next().getUserName()
        }
        return null
    }

    fun getLoginStatus(): Boolean {
        val iterator = loader.iterator()
        while (iterator.hasNext()) {
            return iterator.next().getLoginStatus()
        }
        return false
    }
}