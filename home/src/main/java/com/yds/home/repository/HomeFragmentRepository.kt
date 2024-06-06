package com.yds.home.repository

import android.content.Context
import com.crystallake.base.net.RetrofitClient
import com.yds.core.net.BaseResponse
import com.yds.home.HomeApi
import com.yds.home.db.ArticleDatabase
import com.yds.home.model.ArticleModel
import com.yds.home.model.BannerItem
import com.yds.home.model.HomeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class HomeFragmentRepository {

    suspend fun requestHomeArticleAndBanner(scope: CoroutineScope, num: Int): HomeModel {
        val homeModel = HomeModel()
        val articleResponse = scope.async {
            getHomeArticle(num)
        }
        val bannerResponse = scope.async {
            getBanner()
        }
        homeModel.articleModel = articleResponse.await().data
        homeModel.banner = bannerResponse.await().data
        return homeModel
    }

    suspend fun insertHomeDataToDatabase(context: Context, articleModel: HomeModel?) {
        if (articleModel != null) {
            ArticleDatabase.getInstance(context)?.articleDao()?.insertAll(articleModel)
        }
        articleModel?.banner?.forEach {
            ArticleDatabase.getInstance(context)?.articleDao()?.insertBannerItem(it)
        }
        if (articleModel?.articleModel != null) {
            ArticleDatabase.getInstance(context)?.articleDao()
                ?.insertArticleModel(articleModel.articleModel)
        }
        articleModel?.articleModel?.datas?.forEach {
            ArticleDatabase.getInstance(context)?.articleDao()?.insertBaseArticle(it)
        }
    }

    suspend fun insertArticle(context: Context, articleModel: ArticleModel?) {
        ArticleDatabase.getInstance(context)?.articleDao()?.insertArticleModel(articleModel)
    }

    /**
     * 获取首页文章
     */
    private suspend fun getHomeArticle(num: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(HomeApi::class.java).getHomeArticle(num)
    }

    suspend fun getHomeLoadMore(num: Int, homeModel: HomeModel?): HomeModel? {
        val articleResponse = getHomeArticle(num)
        homeModel?.articleModel?.let { articleModel ->
            //将文章拼接到后面
            articleModel.datas?.addAll(articleResponse.data?.datas ?: mutableListOf())
        } ?: kotlin.run {
            homeModel?.articleModel = articleResponse.data
        }
        return homeModel
    }

    private suspend fun getBanner(): BaseResponse<List<BannerItem>> {
        return RetrofitClient.create(HomeApi::class.java).getBanner()
    }

    suspend fun getCollectList(page: Int): BaseResponse<ArticleModel> {
        return RetrofitClient.create(HomeApi::class.java).getCollectList(page)
    }

    suspend fun collectInsideWebArticle(id: Int): BaseResponse<Any> {
        return RetrofitClient.create(HomeApi::class.java).collectInsideWebArticle(id)
    }

    suspend fun uncollectInsideWebArticle(id: Int): BaseResponse<Any> {
        return RetrofitClient.create(HomeApi::class.java).uncollectInsideWebArticle(id)
    }

}