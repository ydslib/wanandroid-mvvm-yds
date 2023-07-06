package com.yds.project.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment
import com.yds.project.R
import com.yds.project.adapter.ProjectArticleAdapter
import com.yds.project.databinding.FragmentProjectArticleBinding
import com.yds.project.item.ProjectArticleItem
import com.yds.project.vm.ProjectArticleFragmentViewModel
import com.yds.project.vm.ProjectArticleFragmentViewModel.Companion.STATE_LOADING

@Route(path = RouterPath.PROJECT_ARTICLE_FRAGMENT)
class ProjectArticleFragment :
    BaseDataBindingFragment<FragmentProjectArticleBinding, ProjectArticleFragmentViewModel>() {

    private val adapter by lazy {
        ProjectArticleAdapter(mutableListOf())
    }
    private val linearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    private var cid: Int? = null

    override fun createObserver() {
        loginStateChangeObserve {
            cid?.let {
                mViewModel.getProjectData(requireContext(), 1, it, STATE_LOADING)
            }
        }
        mViewModel.projectData.observe(this) {
            adapter.clear()
            adapter.dataList?.addAll(it)
            adapter.notifyDataSetChanged()
        }
        mViewModel.refreshLiveData.observe(this) {
            if (it) {
                mBinding?.smartRefreshLayout?.finishRefresh()
            }
        }
        mViewModel.loadingMoreLiveData.observe(this) {
            if (it) {
                mBinding?.smartRefreshLayout?.finishLoadMore()
            }
        }

        mViewModel.loadingLiveData.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }

    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.smartRefreshLayout?.setOnRefreshListener {
            cid?.let {
                mViewModel.getProjectData(requireContext(), 1, it, ProjectArticleFragmentViewModel.STATE_REFRESH)
            }
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            if (cid != null && mViewModel.curPageLiveData.value != null) {
                val page = mViewModel.curPageLiveData.value ?: 1
                mViewModel.getLoadMoreProjectData(
                    requireContext(),
                    page + 1,
                    cid!!
                )
            }
        }

        mBinding?.recyclerView?.let {
            it.adapter = adapter
            it.layoutManager = linearLayoutManager
        }
        mBinding?.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(requireContext()).resumeRequests()
                } else {
                    Glide.with(requireContext()).pauseRequests()
                }
            }
        })
    }

    override fun lazyLoadData() {
        cid = arguments?.getInt("cid")
        cid?.let {
            mViewModel.getProjectData(requireContext(), 1, it, STATE_LOADING)
        }
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_project_article)
    }

    fun showLoading() {
        mBinding?.loadViewPage?.isVisible = true
        showLoading(mBinding?.loadingView)
    }

    fun hideLoading() {
        mBinding?.loadViewPage?.isVisible = false
        hideLoading(mBinding?.loadingView)
    }
}