package com.crystallake.knowledgehierarchy.api

import com.crystallake.knowledgehierarchy.model.KnowledgeModel
import com.yds.core.net.BaseResponse
import retrofit2.http.GET

interface KnowledgeApi {

    @GET("tree/json")
    suspend fun getKnowledgeData(): BaseResponse<List<KnowledgeModel>>
}