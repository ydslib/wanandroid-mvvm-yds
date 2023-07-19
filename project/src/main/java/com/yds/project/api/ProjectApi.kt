package com.yds.project.api

import com.yds.core.net.BaseResponse
import com.yds.home.fjs.model.ArticleModel
import com.yds.project.model.ProjectTitleModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApi {


    @GET("project/tree/json")
    suspend fun getProjectTitleData(): BaseResponse<List<ProjectTitleModel>>

    @GET("project/list/{page}/json")
    suspend fun getProjectData(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResponse<ArticleModel>
}