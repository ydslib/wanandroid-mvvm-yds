package com.yds.login

import com.yds.core.IUserInfo

class UserInfoProxy : IUserInfo {

    override fun getUserName(): String? {
        return UserInfo.mUserName
    }

    override fun getLoginState(): Boolean {
        return UserInfo.mLoginState
    }

    override fun setLoginState(loginState: Boolean) {
        UserInfo.mLoginState = loginState
    }

    override fun setUserName(userName: String?) {
        UserInfo.mUserName = userName
    }
}