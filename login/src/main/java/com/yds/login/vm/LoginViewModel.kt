package com.yds.login.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.yds.base.bus.Bus
import com.yds.base.bus.BusChannel
import com.yds.core.ILogin
import com.yds.login.model.LoginData
import com.yds.login.repository.LoginRepository

class LoginViewModel : BaseViewModel(), ILogin {

    //输入密码，密码是否可见
    val eyeCanSeeLiveModel = MutableLiveData<Boolean>(false)

    //确认密码，密码是否可见
    val confirmPwdEyeLiveModel = MutableLiveData<Boolean>(false)

    val registerData = MutableLiveData<LoginData>()

    val loginData = MutableLiveData<LoginData>()

    private val loginRepository by lazy {
        LoginRepository()
    }


    fun register(
        username: String,
        password: String,
        repassword: String
    ) {
        request(
            block = {
                loginRepository.register(username, password, repassword)
            },
            success = {
                registerData.value = it.data
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
                loginRepository.login(username, password)
            },
            success = {
                loginData.value = it.data
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
                loginRepository.logout()
            },
            success = {
                Bus.post(BusChannel.LOGIN_STATUS_CHANNEL, false)
            },
            cancel = {}
        )
    }
}