package com.yds.project.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crystallake.base.vm.BaseViewModel
import com.yds.home.fjs.model.BaseArticle
import com.yds.project.ProjectRequest
import com.yds.project.db.ProjectDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectArticleFragmentViewModel : BaseViewModel() {

    companion object {
        const val STATE_LOADING = 0
        const val STATE_REFRESH = 1
        const val STATE_LOAD_MORE = 2
    }

    val projectData = MutableLiveData<List<BaseArticle>>()
    val refreshLiveData = MutableLiveData<Boolean>()
    val loadingMoreLiveData = MutableLiveData<Boolean>()
    val curPageLiveData = MutableLiveData<Int>()
    val loadingLiveData = MutableLiveData<Boolean>()

    fun getProjectData(context: Context, page: Int, cid: Int, state: Int) {
        setState(state, true)
        request(
            block = {
                ProjectRequest.getProjectData(page, cid)
            },
            success = {
                projectData.value = it.data?.datas
                curPageLiveData.value = it.data?.curPage
                it.data?.datas?.forEach { article ->
                    insertBaseArticleData(context, article)
                }
            },
            cancel = {

            },
            complete = {
                setState(state, false)
            }
        )
    }

    fun getLoadMoreProjectData(context: Context, page: Int, cid: Int) {
        setState(STATE_LOAD_MORE, true)
        request(
            block = {
                ProjectRequest.getProjectData(page, cid)
            },
            success = {
                val oldData = mutableListOf<BaseArticle>()
                projectData.value?.let { data ->
                    oldData.addAll(data)
                }
                it.data?.datas?.let {
                    oldData.addAll(it)
                }
                it.data?.datas?.forEach { article ->
                    insertBaseArticleData(context, article)
                }
                projectData.value = oldData
                curPageLiveData.value = it.data?.curPage
            },
            cancel = {

            },
            complete = {
                setState(STATE_LOAD_MORE, false)
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
            STATE_LOADING -> {
                loadingLiveData.value = enable
            }
        }
    }

    private suspend fun insertBaseArticle(context: Context, baseArticle: BaseArticle) {
        ProjectDatabase.getInstance(context)?.getProjectDao()?.insertBaseArticle(baseArticle)
    }

    private fun insertBaseArticleData(context: Context, baseArticle: BaseArticle) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                insertBaseArticle(context, baseArticle)
            }
        }
    }
}