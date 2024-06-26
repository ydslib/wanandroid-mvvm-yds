package com.crystallake.knowledgehierarchy.fragment

import android.os.Bundle
import android.util.Log
import androidx.metrics.performance.JankStats
import androidx.metrics.performance.PerformanceMetricsState
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@Route(path = RouterPath.KNOWLEDGE_ARTICLE_FRAGMENT)
class KnowledgeArticleFragment :
    BaseDataBindingFragment<FragmentKnowledgeArticleBinding, KnowledgeViewModel>() {
    private var cid: Int = 0
    private val layoutManager by lazy {
        LinearLayoutManager(requireContext(), VERTICAL, false)
    }
    private var jankStats: JankStats? = null

    private val jankFrameListener = JankStats.OnFrameListener {
        Log.i("JankStatsSample", "$it")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var metricsStateHolder: PerformanceMetricsState.MetricsStateHolder? = null
        mBinding?.root?.let {
            metricsStateHolder = PerformanceMetricsState.getForHierarchy(it)

        }
        jankStats =
            JankStats.createAndTrack(requireActivity().window, Dispatchers.Default.asExecutor(), jankFrameListener).apply {
                this.jankHeuristicMultiplier = 3f
            }
        metricsStateHolder?.state?.addState("Activity", javaClass.simpleName)
    }

    override fun onResume() {
        super.onResume()
        //启用跟踪功能
        jankStats?.isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        //关闭跟踪功能
        jankStats?.isTrackingEnabled = false
    }

    private val adapter by lazy {
        KnowledgeArticleAdapter(mutableListOf())
    }

    override fun createObserver() {
        mViewModel.articleModelLiveData.observe(this) {
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
            mViewModel.getRefreshKnowledgeArticle(cid)
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            mViewModel.getLoadMoreKnowledgeArticle(cid)
        }

        mBinding?.recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        mViewModel.getLoadKnowledgeArticle(cid)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_knowledge_article)
    }
}