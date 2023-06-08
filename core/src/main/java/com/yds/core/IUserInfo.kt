package com.yds.core

interface IUserInfo {

    fun getUserName(): String?

    fun getLoginState(): Boolean

    fun setLoginState(loginState: Boolean)

    fun setUserName(userName: String?)
}