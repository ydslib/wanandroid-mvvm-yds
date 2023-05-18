package com.crystallake.knowledgehierarchy.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.knowledgehierarchy.R
import com.crystallake.knowledgehierarchy.databinding.FragmentKnowledgeBinding
import com.crystallake.knowledgehierarchy.vm.KnowledgeViewModel
import com.crystallake.resources.RouterPath

@Route(path = RouterPath.KNOWLEDGE_FRAGMENT)
class KnowledgeFragment : DataBindingFragment<FragmentKnowledgeBinding, KnowledgeViewModel>() {

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_knowledge)
    }
}