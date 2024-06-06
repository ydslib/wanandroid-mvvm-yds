package com.crystallake.knowledgehierarchy.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.knowledgehierarchy.repository.KnowledgeRepository
import com.yds.home.model.ArticleModel
import com.yds.project.model.ProjectTitleModel

class KnowledgeViewModel : BaseViewModel() {

    val knowledgeDataLiveData = MutableLiveData<List<ProjectTitleModel>>()
    val refreshLiveData = MutableLiveData<Boolean>()
    val loadingMoreLiveData = MutableLiveData<Boolean>()
    val showLoadingLiveData = MutableLiveData<Boolean>()
    val articleModelLiveData = MutableLiveData<ArticleModel>()
    var curPage = 0

    val knowledgeRepository by lazy { KnowledgeRepository() }

    fun loadProjectTitle(){
        showLoadingLiveData.value = true
        requestProjectTitle {
            showLoadingLiveData.value = false
        }
    }

    fun refreshProjectTitle(){
        refreshLiveData.value = true
        requestProjectTitle {
            refreshLiveData.value = false
        }
    }

    private fun requestProjectTitle(callback: (Boolean) -> Unit){
        request(
            block = {
                knowledgeRepository.getKnowledgeData()
            },
            success = {
                knowledgeDataLiveData.value = it.data
            },
            cancel = {

            },
            complete = {
                callback.invoke(false)
                showLoadingLiveData.value = false
            }
        )
    }

    /**
     * 加载
     */
    fun getLoadKnowledgeArticle(cid: Int) {
        showLoadingLiveData.value = true
        requestKnowledgeArticle(0, cid) {
            showLoadingLiveData.value = false
        }
    }

    /**
     * 刷新
     */
    fun getRefreshKnowledgeArticle(cid: Int) {
        refreshLiveData.value = true
        requestKnowledgeArticle(0, cid) {
            refreshLiveData.value = false
        }
    }

    /**
     * 加载更多
     */
    fun getLoadMoreKnowledgeArticle(cid: Int) {
        loadingMoreLiveData.value = true
        requestKnowledgeArticle(curPage, cid) {
            loadingMoreLiveData.value = false
        }
    }

    private fun requestKnowledgeArticle(page: Int, cid: Int, callback: (Boolean) -> Unit) {
        request(
            block = {
                knowledgeRepository.getKnowledgeArticle(page, cid)
            },
            success = {
                articleModelLiveData.value = it.data
                curPage = it.data?.curPage ?: 0
            },
            cancel = {},
            complete = {
                callback.invoke(false)
            }
        )
    }
}