package com.yds.login

import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.login.api.LoginApi
import com.yds.login.model.LoginData

object LoginRequest {

    suspend fun register(
        username: String,
        password: String,
        repassword: String
    ): BaseResponse<LoginData> {
        return RetrofitClient.create(LoginApi::class.java).register(username, password, repassword)
    }

    suspend fun login(
        username: String,
        password: String,
    ): BaseResponse<LoginData> {
        return RetrofitClient.create(LoginApi::class.java).login(username, password)
    }

    suspend fun logout(){
        RetrofitClient.create(LoginApi::class.java).logout()
    }
}