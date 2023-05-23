package com.crystallake.knowledgehierarchy.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.knowledgehierarchy.R
import com.crystallake.knowledgehierarchy.databinding.FragmentKnowledgeBinding
import com.crystallake.knowledgehierarchy.item.KnowledgeArticleItem
import com.crystallake.knowledgehierarchy.vm.KnowledgeViewModel
import com.crystallake.resources.RouterPath

@Route(path = RouterPath.KNOWLEDGE_FRAGMENT)
class KnowledgeFragment : DataBindingFragment<FragmentKnowledgeBinding, KnowledgeViewModel>() {

    val adapter by lazy {
        MultiDataBindingAdapter()
    }

    val layoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun createObserver() {
        mViewModel.refreshLiveData.observe(this) {
            if (!it) {
                mBinding?.smartRefreshLayout?.finishRefresh()
            }
        }

        mViewModel.knowledgeDataLiveData.observe(this) {
            adapter.clear()
            it?.forEach { model ->
                adapter.addItem(KnowledgeArticleItem(model))
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.recycler?.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
        }

        mBinding?.smartRefreshLayout?.let {
            it.setOnRefreshListener {
                mViewModel.getKnowledgeData(true)
            }
            it.setEnableLoadMore(false)
        }

    }

    override fun lazyLoadData() {
        mViewModel.getKnowledgeData()
    }


    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_knowledge)
    }
}