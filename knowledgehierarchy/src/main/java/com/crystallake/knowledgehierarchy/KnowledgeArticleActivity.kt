package com.crystallake.knowledgehierarchy

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.knowledgehierarchy.databinding.ActivityKnowledgeArticleBinding
import com.crystallake.knowledgehierarchy.vm.KnowledgeViewModel
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingActivity

@Route(path = RouterPath.KNOWLEDGE_ARTICLE_ACTIVITY)
class KnowledgeArticleActivity :
    BaseDataBindingActivity<ActivityKnowledgeArticleBinding, KnowledgeViewModel>() {

    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        position = intent.extras?.getInt("position") ?: 0
        super.onCreate(savedInstanceState)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_knowledge_article)
    }

    override fun initData() {
        super.initData()
        initImmersionBar(R.color.color_333333)
        mViewModel.loadProjectTitle()
    }

    override fun initObser() {
        super.initObser()
        mViewModel.knowledgeDataLiveData.observe(this) {
            mBinding?.title?.text = it[position].name
            val childrens = it[position].children
            val adapter = KnowledgeArticleAdapter(childrens, supportFragmentManager)
            mBinding?.viewPager?.let { vp ->
                vp.adapter = adapter
                mBinding?.tabHeader?.setViewPager(vp)
            }
        }
    }

}