package com.yds.navigation.api

import com.yds.core.net.BaseResponse
import com.yds.navigation.model.NaviModel
import retrofit2.http.GET

interface NaviApi {


    @GET("navi/json")
    suspend fun getNaviTreeData(): BaseResponse<List<NaviModel>>

}