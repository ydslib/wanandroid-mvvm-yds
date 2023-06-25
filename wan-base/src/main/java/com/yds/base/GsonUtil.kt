package com.yds.base

import com.google.gson.Gson

object GsonUtil {
    val gson by lazy {
        Gson()
    }
}