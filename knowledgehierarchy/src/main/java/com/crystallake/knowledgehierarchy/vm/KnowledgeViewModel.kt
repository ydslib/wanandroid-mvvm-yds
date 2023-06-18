package com.crystallake.knowledgehierarchy.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.knowledgehierarchy.KnowledgeRequest
import com.yds.home.model.ArticleModel
import com.yds.project.model.ProjectTitleModel

class KnowledgeViewModel : BaseViewModel() {

    val knowledgeDataLiveData = MutableLiveData<List<ProjectTitleModel>>()
    val refreshLiveData = MutableLiveData<Boolean>()
    val loadingMoreLiveData = MutableLiveData<Boolean>()
    val showLoadingLiveData = MutableLiveData<Boolean>()
    val articleModelLiveData = MutableLiveData<ArticleModel>()
    var mState = 0

    companion object {
        const val REFRESH = 0
        const val LOAD = 2
        const val LOAD_MORE = 1
    }

    fun getKnowledgeData(state: Int) {
        setData(true, state)
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
                setData(false, state)
                showLoadingLiveData.value = false
            }
        )
    }

    private fun setData(requestState: Boolean, state: Int) {
        mState = state
        when (state) {
            REFRESH -> {
                refreshLiveData.value = requestState
            }
            LOAD -> {
                showLoadingLiveData.value = requestState
            }
            LOAD_MORE -> {
                loadingMoreLiveData.value = requestState
            }
        }
    }

    fun getKnowledgeArticle(page: Int, cid: Int, state: Int) {
        setData(true, state)
        request(
            block = {
                KnowledgeRequest.getKnowledgeArticle(page, cid)
            },
            success = {
                articleModelLiveData.value = it.data
            },
            cancel = {},
            complete = {
                setData(false, state)
            }
        )
    }
}