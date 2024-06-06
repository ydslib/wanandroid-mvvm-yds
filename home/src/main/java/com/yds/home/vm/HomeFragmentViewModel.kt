package com.yds.home.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crystallake.base.vm.BaseViewModel
import com.yds.home.model.ArticleModel
import com.yds.home.model.HomeModel
import com.yds.home.repository.HomeFragmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragmentViewModel : BaseViewModel() {

    val homeArticleLiveData = MutableLiveData<HomeModel>()
    val refresh = MutableLiveData<Boolean>()
    val loadMore = MutableLiveData<Boolean>()
    val curPage = MutableLiveData(0)
    val showLoading = MutableLiveData<Boolean>()

    private val homeFragmentRepository by lazy {
        HomeFragmentRepository()
    }

    fun getHomeArticle() {
        showLoading.value = true
        requestArticle(0) {
            showLoading.value = false
        }
    }

    fun getRefreshArticle() {
        refresh.value = true
        requestArticle(0) {
            refresh.value = false
        }
    }

    private fun requestArticle(num: Int, state: (Boolean) -> Unit) {
        request(
            block = {
                homeFragmentRepository.requestHomeArticleAndBanner(this, num)
            },
            success = {
                homeArticleLiveData.value = it
                curPage.value = it.articleModel?.curPage ?: 0
            },
            cancel = {},
            complete = {
                state.invoke(false)
            }
        )
    }

    fun getLoadMoreHomeData(num: Int, context: Context) {
        loadMore.value = true
        request(
            block = {
                val homeModel = homeArticleLiveData.value
                homeFragmentRepository.getHomeLoadMore(num, homeModel)
            },
            success = {
                curPage.value = it?.articleModel?.curPage ?: 0
                homeArticleLiveData.value = it
            },
            cancel = {},
            complete = {
                loadMore.value = false
            }
        )
    }

    fun collectInsideWebArticle(id: Int) {
        request(
            block = {
                homeFragmentRepository.collectInsideWebArticle(id)
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
                homeFragmentRepository.uncollectInsideWebArticle(id)
            },
            success = {
                println(it.data)
            },
            cancel = {}
        )
    }

    private fun insertHomeDataToDatabase(context: Context, articleModel: HomeModel?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                homeFragmentRepository.insertHomeDataToDatabase(context, articleModel)
            }
        }
    }

    private fun insertArticle(context: Context, articleModel: ArticleModel?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                homeFragmentRepository.insertArticle(context, articleModel)
            }
        }
    }

    fun loadAllData(context: Context, state: Int) {
//        setState(true, state)
//        request(block = {
//            ArticleDatabase.getInstance(context)?.articleDao()?.loadAllData()
//        }, success = {
//            if (it == null) {
//                getHomeArticle(curPage.value ?: 0, LOAD, context)
//            } else {
//                homeArticleLiveData.value = it
//                homeModel.articleModel = it.articleModel
//                homeModel.banner = it.banner
//                curPage.value = it.articleModel?.curPage ?: 0
//            }
//        }, cancel = {}, complete = {
//            setState(false, state)
//        })
    }
}