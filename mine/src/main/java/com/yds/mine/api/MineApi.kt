package com.yds.mine.api

import com.yds.core.net.BaseResponse
import com.yds.mine.model.CoinModel
import retrofit2.http.GET

interface MineApi {

    @GET("lg/coin/userinfo/json")
    suspend fun getCoin(): BaseResponse<CoinModel>
}