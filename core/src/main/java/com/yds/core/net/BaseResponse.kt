package com.yds.core.net

data class BaseResponse<T>(
    val errorCode: Int? = null,
    val errorMsg: String? = null,
    val data: T? = null
)