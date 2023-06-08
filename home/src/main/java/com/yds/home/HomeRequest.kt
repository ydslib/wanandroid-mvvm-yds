package com.yds.home

import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.home.model.ArticleModel
import com.yds.home.model.BannerItem

object HomeRequest {

    suspend fun getHomeArticle(num: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(HomeApi::class.java).getHomeArticle(num)
    }

    suspend fun getBanner(): BaseResponse<List<BannerItem>> {
        return RetrofitClient.create(HomeApi::class.java).getBanner()
    }

    suspend fun getCollectList(page: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(HomeApi::class.java).getCollectList(page)
    }

    suspend fun collectInsideWebArticle(id: Int): BaseResponse<Any> {
        return RetrofitClient.create(HomeApi::class.java).collectInsideWebArticle(id)
    }

    suspend fun uncollectInsideWebArticle(id: Int): BaseResponse<Any> {
        return RetrofitClient.create(HomeApi::class.java).uncollectInsideWebArticle(id)
    }
}