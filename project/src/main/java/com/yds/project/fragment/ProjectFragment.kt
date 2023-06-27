package com.yds.project.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.resources.RouterPath
import com.yds.project.R
import com.yds.project.adapter.ProjectPageStateAdapter
import com.yds.project.databinding.FragmentProjectBinding
import com.yds.project.vm.ProjectFragmentViewModel

@Route(path = RouterPath.PROJECT_FRAGMENT)
class ProjectFragment : DataBindingFragment<FragmentProjectBinding, ProjectFragmentViewModel>() {

    override fun createObserver() {
        mViewModel.projectTitle.observe(this) { titleModel ->
            mBinding?.viewPager?.let {
                it.adapter = ProjectPageStateAdapter(childFragmentManager, titleModel)
                mBinding?.tabHeader?.setViewPager(it)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {
        mViewModel.getProjectTitle(requireContext())
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_project)
    }
}