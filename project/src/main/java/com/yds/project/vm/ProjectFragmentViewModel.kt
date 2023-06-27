package com.yds.project.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crystallake.base.vm.BaseViewModel
import com.yds.project.ProjectRequest
import com.yds.project.db.ProjectDatabase
import com.yds.project.model.ProjectTitleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectFragmentViewModel : BaseViewModel() {

    val projectTitle = MutableLiveData<List<ProjectTitleModel>>()

    fun getProjectTitle(context: Context) {
        request(
            block = {
                ProjectRequest.getProjectTitle()
            },
            success = {
                projectTitle.value = it.data
                it.data?.forEach { model ->
                    insertProjectTitleModelData(context, model)
                }
            },
            cancel = {

            },
            complete = {

            }
        )
    }

    private suspend fun insertProjectTitleModel(context: Context, projectTitleModel: ProjectTitleModel) {
        ProjectDatabase.getInstance(context)?.getProjectDao()?.insertProjectTitleModel(projectTitleModel)
    }

    private fun insertProjectTitleModelData(context: Context, projectTitleModel: ProjectTitleModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                insertProjectTitleModel(context, projectTitleModel)
            }
        }
    }
}