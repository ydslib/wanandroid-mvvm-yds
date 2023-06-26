package com.yds.home.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crystallake.base.vm.BaseViewModel
import com.yds.home.HomeRequest
import com.yds.home.db.ArticleDatabase
import com.yds.home.model.ArticleModel
import com.yds.home.model.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragmentViewModel : BaseViewModel() {

    companion object {
        const val REFRESH = 0
        const val LOAD = 2
    }

    val homeArticleLiveData = MutableLiveData<HomeModel>()
    val refresh = MutableLiveData<Boolean>()
    val loadMore = MutableLiveData<Boolean>()
    val curPage = MutableLiveData(0)
    val showLoading = MutableLiveData<Boolean>()
    val homeModel = HomeModel()

    fun getHomeArticle(num: Int, state: Int, context: Context) {
        setState(true, state)
        request(
            block = {
                val homeBean = async {
                    HomeRequest.getHomeArticle(num)
                }
                val bannerBean = async {
                    HomeRequest.getBanner()
                }
                homeModel.articleModel = homeBean.await().data
                homeModel.banner = bannerBean.await().data
                homeModel
            },
            success = {
                insertHomeDataToDatabase(context, it)
                homeArticleLiveData.value = it
                curPage.value = it.articleModel?.curPage ?: 0
            },
            cancel = {},
            complete = {
                setState(false, state)
            }
        )
    }

    fun getLoadMoreHomeData(num: Int, context: Context) {
        loadMore.value = true
        request(
            block = {
                HomeRequest.getHomeArticle(num)
            },
            success = {
                insertArticle(context, it.data)
                homeModel.articleModel?.let { articleModel ->
                    articleModel.datas?.addAll(it.data?.datas ?: mutableListOf())
                } ?: kotlin.run {
                    homeModel.articleModel = it.data
                }
                curPage.value = it.data?.curPage ?: 0
                homeArticleLiveData.value = homeModel
            },
            cancel = {},
            complete = {
                loadMore.value = false
            }
        )
    }

    private fun setState(requestState: Boolean, state: Int) {
        if (state == REFRESH) {
            refresh.value = requestState
        } else if (state == LOAD) {
            showLoading.value = requestState
        }
    }

    fun collectInsideWebArticle(id: Int) {
        request(
            block = {
                HomeRequest.collectInsideWebArticle(id)
            },
            success = {
                println(it.data)
            },
            cancel = {}
        )
    }

    fun uncollectInsideWebArticle(id: Int) {
        request(
            block = {
                HomeRequest.uncollectInsideWebArticle(id)
            },
            success = {
                println(it.data)
            },
            cancel = {}
        )
    }

    fun insertHomeDataToDatabase(context: Context, articleModel: HomeModel?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
        }
    }

    fun insertArticle(context: Context, articleModel: ArticleModel?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ArticleDatabase.getInstance(context)?.articleDao()?.insertArticleModel(articleModel)
            }
        }
    }

    fun loadAllData(context: Context, state: Int) {
        setState(true, state)
        request(block = {
            ArticleDatabase.getInstance(context)?.articleDao()?.loadAllData()
        }, success = {
            println("loadAllData:$it")
            if (it == null) {
                getHomeArticle(curPage.value ?: 0, LOAD, context)
            } else {
                homeArticleLiveData.value = it
                homeModel.articleModel = it.articleModel
                homeModel.banner = it.banner
                curPage.value = it.articleModel?.curPage ?: 0
            }
        }, cancel = {}, complete = {
            setState(false, state)
        })
    }
}