package com.yds.mine.api

import com.yds.core.net.BaseResponse
import com.yds.home.model.ArticleModel
import com.yds.mine.model.CoinModel
import retrofit2.http.GET
import retrofit2.http.Path

interface MineApi {

    @GET("lg/coin/userinfo/json")
    suspend fun getCoin(): BaseResponse<CoinModel>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectArticle(@Path("page") page: Int): BaseResponse<ArticleModel>
}