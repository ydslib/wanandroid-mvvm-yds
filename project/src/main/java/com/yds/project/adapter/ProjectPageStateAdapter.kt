package com.yds.project.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.resources.RouterPath
import com.yds.project.model.ProjectTitleModel

class ProjectPageStateAdapter(
    fragmentManager: FragmentManager,
    var titleData: List<ProjectTitleModel>
) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return titleData.size
    }

    override fun getItem(position: Int): Fragment {
        return ARouter.getInstance().build(RouterPath.PROJECT_ARTICLE_FRAGMENT)
            .withInt("cid", titleData[position].id ?: 0)
            .navigation() as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position < titleData.size) {
            return titleData?.get(position)?.name
        }
        return ""
    }
}