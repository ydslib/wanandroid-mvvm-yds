package com.crystallake.knowledgehierarchy.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.knowledgehierarchy.R
import com.crystallake.knowledgehierarchy.databinding.FragmentKnowledgeArticleBinding
import com.crystallake.knowledgehierarchy.vm.KnowledgeViewModel
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment

@Route(path = RouterPath.KNOWLEDGE_ARTICLE_FRAGMENT)
class KnowledgeArticleFragment :
    BaseDataBindingFragment<FragmentKnowledgeArticleBinding, KnowledgeViewModel>() {
    private var cid: Int = 0
    private var page: Int = 0
    override fun createObserver() {
        mViewModel.articleModelLiveData.observe(this) {
            page = it?.curPage ?: 0

        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        cid = arguments?.getInt("cid") ?: 0
    }

    override fun lazyLoadData() {
        mViewModel.getKnowledgeArticle(0, cid)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_knowledge_article)
    }
}