package com.yds.login

import com.yds.core.IUserInfo

class UserInfoProxy : IUserInfo {

    override fun getUserName(): String? {
        return UserInfo.mUserName
    }

    override fun getLoginStatus(): Boolean {
        return UserInfo.mLoginState
    }
}