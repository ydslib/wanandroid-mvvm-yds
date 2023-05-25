package com.yds.project.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.resources.RouterPath
import com.yds.project.R
import com.yds.project.databinding.FragmentProjectArticleBinding
import com.yds.project.item.ProjectArticleItem
import com.yds.project.vm.ProjectArticleFragmentViewModel
import com.yds.project.vm.ProjectArticleFragmentViewModel.Companion.STATE_LOADING

@Route(path = RouterPath.PROJECT_ARTICLE_FRAGMENT)
class ProjectArticleFragment :
    DataBindingFragment<FragmentProjectArticleBinding, ProjectArticleFragmentViewModel>() {

    private val adapter by lazy {
        MultiDataBindingAdapter()
    }
    private val linearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    private var cid: Int? = null

    override fun createObserver() {
        mViewModel.projectData.observe(this) {
            adapter.clear()
            it.forEach { model ->
                adapter.addItem(ProjectArticleItem(model))
            }
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
                mViewModel.getProjectData(1, it, ProjectArticleFragmentViewModel.STATE_REFRESH)
            }
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            if (cid != null && mViewModel.curPageLiveData.value != null) {
                val page = mViewModel.curPageLiveData.value ?: 1
                mViewModel.getLoadMoreProjectData(
                    page + 1,
                    cid!!
                )
            }
        }

        mBinding?.recyclerView?.let {
            it.adapter = adapter
            it.layoutManager = linearLayoutManager
        }
    }

    override fun lazyLoadData() {
        cid = arguments?.getInt("cid")
        cid?.let {
            mViewModel.getProjectData(1, it, STATE_LOADING)
        }
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_project_article)
    }

    fun showLoading() {
        mBinding?.loadViewPage?.isVisible = true
        mBinding?.loadingView?.let {
            it.setAnimation("loading_bus.json")
            it.repeatCount = LottieDrawable.INFINITE
            it.repeatMode = LottieDrawable.REVERSE
            it.playAnimation()
        }

    }

    fun hideLoading() {
        mBinding?.loadViewPage?.isVisible = false
        mBinding?.loadingView?.pauseAnimation()
    }
}