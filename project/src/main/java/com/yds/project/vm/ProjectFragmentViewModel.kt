package com.yds.project.vm

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel
import com.yds.project.ProjectRequest
import com.yds.project.model.ProjectTitleModel

class ProjectFragmentViewModel : BaseViewModel() {

    val projectTitle = MutableLiveData<List<ProjectTitleModel>>()

    fun getProjectTitle() {
        request(
            block = {
                ProjectRequest.getProjectTitle()
            },
            success = {
                projectTitle.value = it.data
            },
            cancel = {

            },
            complete = {

            }
        )
    }
}