package com.yds.login.repository

import com.blankj.utilcode.util.SPUtils
import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.login.UserInfo
import com.yds.login.api.LoginApi
import com.yds.login.model.LoginData

class LoginRepository {

    suspend fun register(
        username: String,
        password: String,
        repassword: String
    ): BaseResponse<LoginData> {
        val loginData = RetrofitClient.create(LoginApi::class.java).register(username, password, repassword)
        UserInfo.mUserName = loginData.data?.username
        UserInfo.mLoginState = true
        SPUtils.getInstance("UserInfo").put("username", UserInfo.mUserName)
        SPUtils.getInstance("UserInfo").put("loginState", UserInfo.mLoginState)
        return loginData
    }

    suspend fun login(
        username: String,
        password: String,
    ): BaseResponse<LoginData> {
        val loginData = RetrofitClient.create(LoginApi::class.java).login(username, password)
        UserInfo.mUserName = loginData.data?.username
        UserInfo.mLoginState = true
        SPUtils.getInstance("UserInfo").put("username", UserInfo.mUserName)
        SPUtils.getInstance("UserInfo").put("loginState", UserInfo.mLoginState)
        return loginData
    }

    suspend fun logout() {
        RetrofitClient.create(LoginApi::class.java).logout()
        UserInfo.mLoginState = false
        SPUtils.getInstance("UserInfo").clear()
    }

}