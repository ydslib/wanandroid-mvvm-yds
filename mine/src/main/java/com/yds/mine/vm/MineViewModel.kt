package com.yds.mine.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.yds.home.model.ArticleModel
import com.yds.mine.MineRequest
import com.yds.mine.model.CoinModel

class MineViewModel : BaseViewModel() {

    val coinLiveData = MutableLiveData<CoinModel>()
    val collectLiveData = MutableLiveData<ArticleModel>()
    val collectTotal = MutableLiveData<Int>()

    fun getCoin() {
        request(
            block = {
                MineRequest.getCoin()
            },
            success = {
                coinLiveData.value = it.data
                println(it.data.toString())
            },
            cancel = {},
            complete = {}
        )
    }

    fun getCollectList(page: Int) {
        request(
            block = {
                MineRequest.getCollectList(page)
            },
            success = {
                collectLiveData.value = it.data
                collectTotal.value = it.data?.total
            },
            cancel = {},
            complete = {}
        )
    }
}