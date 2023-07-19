package com.yds.mine

import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.home.fjs.model.ArticleModel
import com.yds.mine.api.MineApi
import com.yds.mine.model.CoinModel

object MineRequest {

    suspend fun getCoin(): BaseResponse<CoinModel> {
        return RetrofitClient.create(MineApi::class.java).getCoin()
    }

    suspend fun getCollectList(page: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(MineApi::class.java).getCollectArticle(page)
    }
}