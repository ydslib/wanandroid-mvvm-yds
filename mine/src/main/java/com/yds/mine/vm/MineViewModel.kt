package com.yds.mine.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.yds.mine.MineRequest
import com.yds.mine.model.CoinModel

class MineViewModel : BaseViewModel() {

    val coinLiveData = MutableLiveData<CoinModel>()

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
}