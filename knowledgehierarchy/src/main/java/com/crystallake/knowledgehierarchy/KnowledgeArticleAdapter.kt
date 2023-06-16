package com.crystallake.knowledgehierarchy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.resources.RouterPath
import com.yds.project.model.ProjectTitleModel

class KnowledgeArticleAdapter(
    val dataList: List<ProjectTitleModel>?,
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return dataList?.size ?: 0
    }

    override fun getItem(position: Int): Fragment {
        return ARouter.getInstance().build(RouterPath.KNOWLEDGE_ARTICLE_FRAGMENT)
            .apply {
                extras.putInt("cid", dataList?.get(position)?.id ?: 0)
            }.navigation() as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position < (dataList?.size ?: 0)) {
            return dataList?.get(position)?.name
        }
        return ""
    }
}