package com.yds.home.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel

class HomeFragmentViewModel : BaseViewModel() {

    val menuRotation: MutableLiveData<Boolean> = MutableLiveData(false)

}