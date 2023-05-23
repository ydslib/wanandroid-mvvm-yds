package com.crystallake.knowledgehierarchy

import com.crystallake.base.net.RetrofitClient
import com.crystallake.knowledgehierarchy.api.KnowledgeApi
import com.crystallake.knowledgehierarchy.model.KnowledgeModel
import com.yds.core.net.BaseResponse

object KnowledgeRequest {

    suspend fun getKnowledgeData(): BaseResponse<List<KnowledgeModel>> {
        return RetrofitClient.create(KnowledgeApi::class.java).getKnowledgeData()
    }
}