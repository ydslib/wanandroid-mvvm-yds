package com.yds.navigation

import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.navigation.api.NaviApi
import com.yds.navigation.model.NaviModel

object NavigationRequest {


    suspend fun getNaviTreeData(): BaseResponse<List<NaviModel>> {
        return RetrofitClient.create(NaviApi::class.java).getNaviTreeData()
    }

}