package com.crystallake.knowledgehierarchy.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.knowledgehierarchy.R
import com.crystallake.knowledgehierarchy.adapter.KnowledgeAdapter
import com.crystallake.knowledgehierarchy.databinding.FragmentKnowledgeBinding
import com.crystallake.knowledgehierarchy.vm.KnowledgeViewModel
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment

@Route(path = RouterPath.KNOWLEDGE_FRAGMENT)
class KnowledgeFragment : BaseDataBindingFragment<FragmentKnowledgeBinding, KnowledgeViewModel>() {

    private val adapter by lazy {
        KnowledgeAdapter(mutableListOf())
    }

    private val layoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun createObserver() {

        loginStateChangeObserve {
            lazyLoadData()
        }

        mViewModel.refreshLiveData.observe(this) {
            if (!it) {
                mBinding?.smartRefreshLayout?.finishRefresh()
            }
        }

        mViewModel.knowledgeDataLiveData.observe(this) {
            adapter.clear()
            adapter.dataList.addAll(it)
            adapter.notifyDataSetChanged()
        }

        mViewModel.showLoadingLiveData.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.recycler?.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
        }

        mBinding?.smartRefreshLayout?.let {
            it.setOnRefreshListener {
                mViewModel.refreshProjectTitle()
            }
            it.setEnableLoadMore(false)
        }

    }

    override fun lazyLoadData() {
        mViewModel.loadProjectTitle()
    }


    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_knowledge)
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