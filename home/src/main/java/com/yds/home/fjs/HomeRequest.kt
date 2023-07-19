package com.yds.home.fjs

import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.home.fjs.model.ArticleModel
import com.yds.home.fjs.model.BannerItemData

object HomeRequest {

    suspend fun getHomeArticle(num: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(HomeApi::class.java).getHomeArticle(num)
    }

    suspend fun getBanner(): BaseResponse<List<BannerItemData>> {
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