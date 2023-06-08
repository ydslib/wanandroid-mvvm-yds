package com.crystallake.knowledgehierarchy.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.knowledgehierarchy.KnowledgeRequest
import com.crystallake.knowledgehierarchy.model.KnowledgeModel

class KnowledgeViewModel : BaseViewModel() {

    val knowledgeDataLiveData = MutableLiveData<List<KnowledgeModel>>()
    val refreshLiveData = MutableLiveData<Boolean>()
    val showLoadingLiveData = MutableLiveData<Boolean>()

    fun getKnowledgeData(refresh: Boolean = false) {
        if (!refresh) {
            showLoadingLiveData.value = true
        }
        setRefreshLiveData(refresh, true)
        request(
            block = {
                KnowledgeRequest.getKnowledgeData()
            },
            success = {
                knowledgeDataLiveData.value = it.data
            },
            cancel = {

            },
            complete = {
                setRefreshLiveData(refresh, false)
                showLoadingLiveData.value = false
            }
        )
    }

    fun setRefreshLiveData(refresh: Boolean, state: Boolean) {
        if (refresh) {
            refreshLiveData.value = state
        }
    }
}