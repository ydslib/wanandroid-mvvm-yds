package com.yds.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.resources.RouterPath

class NavigationFragmentStateAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ARouter.getInstance().build(RouterPath.HOME_FRAGMENT)
                .navigation() as Fragment
            1 -> return ARouter.getInstance().build(RouterPath.KNOWLEDGE_FRAGMENT)
                .navigation() as Fragment
            2 -> return ARouter.getInstance().build(RouterPath.MINE_FRAGMENT)
                .navigation() as Fragment
            3 -> return ARouter.getInstance().build(RouterPath.NAVIGATION_FRAGMENT)
                .navigation() as Fragment
        }
        return ARouter.getInstance().build(RouterPath.HOME_FRAGMENT).navigation() as Fragment
    }
}