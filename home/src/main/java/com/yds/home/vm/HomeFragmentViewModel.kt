package com.yds.home.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.yds.home.HomeRequest
import com.yds.home.model.ArticleModel
import com.yds.home.model.HomeModel
import kotlinx.coroutines.async

class HomeFragmentViewModel : BaseViewModel() {

    val homeArticleLiveData = MutableLiveData<HomeModel>()
    val refresh = MutableLiveData<Boolean>()
    val loadMore = MutableLiveData<Boolean>()
    val curPage = MutableLiveData(0)
    val showLoading = MutableLiveData<Boolean>()
    val homeModel = HomeModel()

    fun getHomeArticle(num: Int, state: Int) {
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
                homeArticleLiveData.value = it
            },
            cancel = {},
            complete = {
                setState(false, state)
            }
        )
    }

    fun getLoadMoreHomeData(num: Int) {
        loadMore.value = true
        request(
            block = {
                HomeRequest.getHomeArticle(num)
            },
            success = {
                homeModel.articleModel?.let {
                    it.datas?.addAll(it.datas)
                }
                homeArticleLiveData.value = homeModel
            },
            cancel = {},
            complete = {
                loadMore.value = false
            }
        )
    }

    private fun setState(requestState: Boolean, state: Int) {
        if (state == 0) {
            refresh.value = requestState
        } else if(state == 2){
            showLoading.value = requestState
        }
    }
}