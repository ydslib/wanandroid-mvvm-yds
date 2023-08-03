package com.crystallake.sdklog.util

import com.google.gson.GsonBuilder

object JsonHelper {

    val GSON by lazy {
        GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
    }

    @JvmStatic
    fun <T> fromJson(jsonContent: String?, clazz: Class<T>): T? {
        return GSON.fromJson(jsonContent, clazz)
    }

    @JvmStatic
    fun <T> toJson(any: T): String {
        return GSON.toJson(any)
    }
}