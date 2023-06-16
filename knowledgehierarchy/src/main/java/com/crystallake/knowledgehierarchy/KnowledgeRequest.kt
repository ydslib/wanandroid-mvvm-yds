package com.crystallake.knowledgehierarchy

import com.crystallake.base.net.RetrofitClient
import com.crystallake.knowledgehierarchy.api.KnowledgeApi
import com.yds.core.net.BaseResponse
import com.yds.home.model.ArticleModel
import com.yds.project.model.ProjectTitleModel

object KnowledgeRequest {

    suspend fun getKnowledgeData(): BaseResponse<List<ProjectTitleModel>> {
        return RetrofitClient.create(KnowledgeApi::class.java).getKnowledgeData()
    }

    suspend fun getKnowledgeArticle(page: Int, cid: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(KnowledgeApi::class.java).getKnowledgeArticle(page, cid)
    }
}