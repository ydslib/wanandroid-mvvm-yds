package com.yds.login.vm

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.SPUtils
import com.crystallake.base.vm.BaseViewModel
import com.yds.base.bus.Bus
import com.yds.base.bus.BusChannel
import com.yds.core.ILogin
import com.yds.login.LoginRequest
import com.yds.login.UserInfo
import com.yds.login.model.LoginData

class LoginViewModel : BaseViewModel(), ILogin {

    //输入密码，密码是否可见
    val eyeCanSeeLiveModel = MutableLiveData<Boolean>(false)

    //确认密码，密码是否可见
    val confirmPwdEyeLiveModel = MutableLiveData<Boolean>(false)

    val registerData = MutableLiveData<LoginData>()

    val loginData = MutableLiveData<LoginData>()


    fun register(
        username: String,
        password: String,
        repassword: String
    ) {
        request(
            block = {
                LoginRequest.register(username, password, repassword)
            },
            success = {
                registerData.value = it.data
                UserInfo.mUserName = it.data?.username
                UserInfo.mLoginState = true
                SPUtils.getInstance("UserInfo").put("username", UserInfo.mUserName)
                SPUtils.getInstance("UserInfo").put("loginState", UserInfo.mLoginState)
                Bus.post(BusChannel.LOGIN_STATUS_CHANNEL, true)
            },
            cancel = {

            },
            complete = {

            }
        )
    }

    fun login(
        username: String,
        password: String
    ) {
        request(
            block = {
                LoginRequest.login(username, password)
            },
            success = {
                loginData.value = it.data
                UserInfo.mUserName = it.data?.username
                UserInfo.mLoginState = true
                SPUtils.getInstance("UserInfo").put("username", UserInfo.mUserName)
                SPUtils.getInstance("UserInfo").put("loginState", UserInfo.mLoginState)
                Bus.post(BusChannel.LOGIN_STATUS_CHANNEL, true)
            },
            cancel = {

            },
            complete = {

            }
        )
    }

    override fun logout() {
        request(
            block = {
                LoginRequest.logout()
            },
            success = {
                UserInfo.mLoginState = false
                SPUtils.getInstance("UserInfo").clear()
                Bus.post(BusChannel.LOGIN_STATUS_CHANNEL, false)
            },
            cancel = {}
        )
    }
}