package com.crystallake.knowledgehierarchy.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.knowledgehierarchy.R
import com.crystallake.knowledgehierarchy.adapter.KnowledgeArticleAdapter
import com.crystallake.knowledgehierarchy.databinding.FragmentKnowledgeArticleBinding
import com.crystallake.knowledgehierarchy.vm.KnowledgeViewModel
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment

@Route(path = RouterPath.KNOWLEDGE_ARTICLE_FRAGMENT)
class KnowledgeArticleFragment :
    BaseDataBindingFragment<FragmentKnowledgeArticleBinding, KnowledgeViewModel>() {
    private var cid: Int = 0
    private var page: Int = 0
    private val layoutManager by lazy {
        LinearLayoutManager(requireContext(), VERTICAL, false)
    }

    private val adapter by lazy {
        KnowledgeArticleAdapter(mutableListOf())
    }

    override fun createObserver() {
        mViewModel.articleModelLiveData.observe(this) {
            page = it?.curPage ?: 0
            it.datas?.let {
                adapter.update(it)
            }

            mViewModel.refreshLiveData.observe(this) {
                mBinding?.smartRefreshLayout?.finishRefresh()
            }
            mViewModel.loadingMoreLiveData.observe(this) {
                mBinding?.smartRefreshLayout?.finishLoadMore()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        cid = arguments?.getInt("cid") ?: 0
        mBinding?.recycler?.layoutManager = layoutManager
        mBinding?.recycler?.adapter = adapter
        mBinding?.recycler?.setHasFixedSize(true)

        mBinding?.smartRefreshLayout?.setOnRefreshListener {
            mViewModel.getKnowledgeArticle(0, cid, KnowledgeViewModel.REFRESH)
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            mViewModel.getKnowledgeArticle(page, cid, KnowledgeViewModel.LOAD_MORE)
        }

        mBinding?.recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!requireActivity().isDestroyed && !requireActivity().isFinishing) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Glide.with(requireContext()).resumeRequests()
                    } else {
                        Glide.with(requireContext()).pauseRequests()
                    }
                }
            }
        })
    }

    override fun lazyLoadData() {
        mViewModel.getKnowledgeArticle(0, cid, KnowledgeViewModel.LOAD)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_knowledge_article)
    }
}