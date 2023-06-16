package com.crystallake.knowledgehierarchy.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.knowledgehierarchy.KnowledgeRequest
import com.yds.home.model.ArticleModel
import com.yds.project.model.ProjectTitleModel

class KnowledgeViewModel : BaseViewModel() {

    val knowledgeDataLiveData = MutableLiveData<List<ProjectTitleModel>>()
    val refreshLiveData = MutableLiveData<Boolean>()
    val showLoadingLiveData = MutableLiveData<Boolean>()
    val articleModelLiveData = MutableLiveData<ArticleModel>()

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

    private fun setRefreshLiveData(refresh: Boolean, state: Boolean) {
        if (refresh) {
            refreshLiveData.value = state
        }
    }

    fun getKnowledgeArticle(page: Int, cid: Int) {
        request(
            block = {
                KnowledgeRequest.getKnowledgeArticle(page, cid)
            },
            success = {
                articleModelLiveData.value = it.data
            },
            cancel = {},
            complete = {}
        )
    }
}