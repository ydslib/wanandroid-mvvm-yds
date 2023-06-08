package com.yds.mine

import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.mine.api.MineApi
import com.yds.mine.model.CoinModel

object MineRequest {

    suspend fun getCoin(): BaseResponse<CoinModel> {
        return RetrofitClient.create(MineApi::class.java).getCoin()
    }
}