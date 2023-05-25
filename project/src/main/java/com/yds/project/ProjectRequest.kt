package com.yds.project

import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.project.api.ProjectApi
import com.yds.project.model.ArticleModel
import com.yds.project.model.ProjectTitleModel

object ProjectRequest {

    suspend fun getProjectTitle(): BaseResponse<List<ProjectTitleModel>> {
        return RetrofitClient.create(ProjectApi::class.java).getProjectTitleData()
    }

    suspend fun getProjectData(page: Int, cid: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(ProjectApi::class.java).getProjectData(page, cid)
    }
}