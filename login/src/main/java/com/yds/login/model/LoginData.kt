package com.yds.login.model

data class LoginData(
    val username: String?,
    val password: String?,
    val icon: String?,
    val type: Int?,
    val collectIds: List<Int>?,
    val nickname: String?,
    val publicName: String?
)
