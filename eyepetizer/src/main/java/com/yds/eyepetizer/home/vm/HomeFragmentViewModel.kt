package com.yds.eyepetizer.home.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.base.vm.RequestMethod
import com.yds.eyepetizer.api.EyeRequest
import com.yds.eyepetizer.home.model.HomeModel

class HomeFragmentViewModel : BaseViewModel() {

    val homeBannerLiveData = MediatorLiveData<HomeModel>()

    fun getHomeBanner(method: RequestMethod) {
        homeBannerLiveData.addSource(flowEx(method) {
            EyeRequest.getHomeBanner()
        }) {
            homeBannerLiveData.value = it
        }
    }

}