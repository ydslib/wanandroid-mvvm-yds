package com.yds.project.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.yds.project.ProjectRequest
import com.yds.project.model.ArticleModel
import com.yds.project.model.BaseArticle

class ProjectArticleFragmentViewModel : BaseViewModel() {

    companion object {
        const val STATE_REFRESH = 1
        const val STATE_LOAD_MORE = 2
    }

    val projectData = MutableLiveData<List<BaseArticle>>()
    val refreshLiveData = MutableLiveData<Boolean>()
    val loadingMoreLiveData = MutableLiveData<Boolean>()
    val curPageLiveData = MutableLiveData<Int>()

    fun getProjectData(page: Int, cid: Int, state: Int) {
        setState(state, true)
        request(
            block = {
                ProjectRequest.getProjectData(page, cid)
            },
            success = {
                projectData.value = it.data?.datas
                curPageLiveData.value = it.data?.curPage
            },
            cancel = {

            },
            complete = {
                setState(state, false)
            }
        )
    }

    fun setState(state: Int, enable: Boolean) {
        when (state) {
            STATE_REFRESH -> {
                refreshLiveData.value = enable
            }
            STATE_LOAD_MORE -> {
                loadingMoreLiveData.value = enable
            }
        }
    }
}