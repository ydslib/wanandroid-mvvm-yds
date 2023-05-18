package com.crystallake.knowledgehierarchy

import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.knowledgehierarchy.databinding.ActivityKnowledgeMainBinding


class KnowledgeActivity : DataBindingActivity<ActivityKnowledgeMainBinding, BaseViewModel>() {
    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_knowledge_main)
    }
}