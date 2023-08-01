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
    val homeDailyLiveData = MediatorLiveData<List<ProviderMultiModel>>()
    private var mNextUrl: String? = null

    fun getHomeBanner(method: RequestMethod) {
        homeBannerLiveData.addSource(flowEx(method) {
            EyeRequest.getHomeBanner()
        }) {
            mNextUrl = it.nextPageUrl
            val list = it?.itemList ?: mutableListOf()
            list.removeAll {
                it.type == TEXT_HEADER_TYPE
            }
            val providerMultiModel = ProviderMultiModel(type = ProviderMultiModel.Type.TYPE_BANNER, items = list)
            homeBannerLiveData.value = providerMultiModel
        }
    }

    fun getHomeList(method: RequestMethod) {
        if (mNextUrl.isNullOrEmpty()) {
            return
        }
        homeDailyLiveData.addSource(flowEx(method) {
            val homeModel = EyeRequest.getHomeList(mNextUrl!!)
            mNextUrl = homeModel.nextPageUrl
            val list = homeModel.itemList
            val providerMultiModels = mutableListOf<ProviderMultiModel>()
            list.forEach {
                if (it.type == TEXT_HEADER_TYPE) {
                    providerMultiModels.add(
                        ProviderMultiModel(
                            type = ProviderMultiModel.Type.TYPE_TITLE,
                            item = it
                        )
                    )
                } else {
                    providerMultiModels.add(
                        ProviderMultiModel(
                            type = ProviderMultiModel.Type.TYPE_IMAGE,
                            item = it
                        )
                    )
                }
            }
            providerMultiModels
        }) {
            homeDailyLiveData.value = it
        }
    }

}