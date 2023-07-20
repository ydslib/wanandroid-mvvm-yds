package com.yds.eyepetizer.api

import com.crystallake.base.net.RetrofitClient
import com.yds.eyepetizer.home.model.HomeModel
import com.yds.eyepetizer.home.model.Issue

object EyeRequest {

    suspend fun getHomeBanner(): HomeModel {
        return RetrofitClient.create(EyeApi::class.java).getHomeBanner()
    }

    suspend fun getHomeList(url: String): HomeModel {
        return RetrofitClient.create(EyeApi::class.java).getHomeList(url)
    }

    suspend fun getKeyWordList(): List<String> {
        return RetrofitClient.create(EyeApi::class.java).getKeyWordList()
    }

    suspend fun searchVideoList(keyword: String): Issue {
        return RetrofitClient.create(EyeApi::class.java).searchVideoList(keyword)
    }

    suspend fun getMoreSearchVideoList(url: String): Issue {
        return RetrofitClient.create(EyeApi::class.java).getMoreSearchVideoList(url)
    }
}