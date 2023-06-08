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

    fun getLoginState(): Boolean {
        val iterator = loader.iterator()
        while (iterator.hasNext()) {
            return iterator.next().getLoginState()
        }
        return false
    }

    fun setLoginState(loginState: Boolean) {
        val iterator = loader.iterator()
        while (iterator.hasNext()) {
            return iterator.next().setLoginState(loginState)
        }
    }

    fun setUserName(userName: String) {
        val iterator = loader.iterator()
        while (iterator.hasNext()) {
            return iterator.next().setUserName(userName)
        }
    }
}