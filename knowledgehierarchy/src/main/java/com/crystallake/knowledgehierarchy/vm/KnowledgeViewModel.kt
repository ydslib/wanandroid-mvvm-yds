package com.crystallake.knowledgehierarchy.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.knowledgehierarchy.KnowledgeRequest
import com.crystallake.knowledgehierarchy.model.KnowledgeModel

class KnowledgeViewModel : BaseViewModel() {

    val knowledgeDataLiveData = MutableLiveData<List<KnowledgeModel>>()
    val refreshLiveData = MutableLiveData<Boolean>()

    fun getKnowledgeData(refresh: Boolean = false) {
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
            }
        )
    }

    fun setRefreshLiveData(refresh: Boolean, state: Boolean) {
        if (refresh) {
            refreshLiveData.value = state
        }
    }
}