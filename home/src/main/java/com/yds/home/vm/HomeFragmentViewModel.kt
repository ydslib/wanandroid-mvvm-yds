package com.yds.home.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.yds.home.HomeRequest
import com.yds.home.model.ArticleModel

class HomeFragmentViewModel : BaseViewModel() {

    val homeArticleLiveData = MutableLiveData<ArticleModel>()

    fun getHomeArticle(num: Int) {
        request(
            block = {
                HomeRequest.getHomeArticle(0)
            },
            success = {
                homeArticleLiveData.value = it.data
            },
            cancel = {}
        )
    }
}