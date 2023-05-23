package com.yds.navigation.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.yds.navigation.NavigationRequest
import com.yds.navigation.model.NaviModel

class NavigationViewModel : BaseViewModel() {

    val naviData = MutableLiveData<List<NaviModel>>()

    fun getNaviTitleData() {
        request(
            block = {
                NavigationRequest.getNaviTreeData()
            },
            success = {
                naviData.value = it.data
            },
            cancel = {

            },
            complete = {

            }
        )
    }

}