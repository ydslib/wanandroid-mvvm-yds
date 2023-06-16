package com.crystallake.knowledgehierarchy.api

import com.yds.core.net.BaseResponse
import com.yds.home.model.ArticleModel
import com.yds.project.model.ProjectTitleModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KnowledgeApi {

    @GET("tree/json")
    suspend fun getKnowledgeData(): BaseResponse<List<ProjectTitleModel>>

    @GET("article/list/{page}/json")
    suspend fun getKnowledgeArticle(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResponse<ArticleModel>
}