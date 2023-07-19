package com.yds.home.fjs

import com.yds.core.net.BaseResponse
import com.yds.home.fjs.model.ArticleModel
import com.yds.home.fjs.model.BannerItemData
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApi {

    @GET("article/list/{num}/json")
    suspend fun getHomeArticle(@Path("num") num: Int): BaseResponse<ArticleModel>

    @GET("banner/json")
    suspend fun getBanner(): BaseResponse<List<BannerItemData>>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(@Path("page") page: Int): BaseResponse<ArticleModel>

    @POST("lg/collect/{id}/json")
    suspend fun collectInsideWebArticle(@Path("id") id: Int): BaseResponse<Any>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollectInsideWebArticle(@Path("id") id: Int): BaseResponse<Any>

}