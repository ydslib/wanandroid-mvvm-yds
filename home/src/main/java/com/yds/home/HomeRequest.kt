package com.yds.home

import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.home.model.ArticleModel

object HomeRequest {

    suspend fun getHomeArticle(num: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(HomeApi::class.java).getHomeArticle(num)
    }
}