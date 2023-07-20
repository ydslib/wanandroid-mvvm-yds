package com.yds.eyepetizer.api

import com.yds.eyepetizer.home.model.HomeModel
import com.yds.eyepetizer.home.model.Issue
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface EyeApi {
    @GET("v5/index/tab/feed")
    suspend fun getHomeBanner(): HomeModel

    @GET
    suspend fun getHomeList(@Url url: String): HomeModel

    @GET("v3/queries/hot")
    suspend fun getKeyWordList(): List<String>

    @GET("v1/search")
    suspend fun searchVideoList(@Query("query") keyword: String): Issue

    @GET
    suspend fun getMoreSearchVideoList(@Url url: String): Issue
}