package com.yds.eyepetizer.home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.base.vm.RequestMethod
import com.yds.eyepetizer.api.EyeRequest
import com.yds.eyepetizer.home.model.HomeModel
import com.yds.eyepetizer.home.model.ProviderMultiModel

class HomeFragmentViewModel : BaseViewModel() {

    private val TEXT_HEADER_TYPE = "textCard"

    val homeBannerLiveData = MediatorLiveData<ProviderMultiModel>()

    fun getHomeBanner(method: RequestMethod) {
        homeBannerLiveData.addSource(flowEx(method) {
            EyeRequest.getHomeBanner()
        }) {
            val list = it?.itemList ?: mutableListOf()
            list.removeAll {
                it.type == TEXT_HEADER_TYPE
            }
            val providerMultiModel = ProviderMultiModel(type = ProviderMultiModel.Type.TYPE_BANNER, items = list)
            homeBannerLiveData.value = providerMultiModel
        }
    }

}