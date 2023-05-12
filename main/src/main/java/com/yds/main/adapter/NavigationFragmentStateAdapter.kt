package com.yds.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.resources.RouterPath

class NavigationFragmentStateAdapter(
    val fragmentActivity: FragmentActivity,
    val size: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0-> return ARouter.getInstance().build(RouterPath.HOME_FRAGMENT).navigation() as Fragment
            1-> return ARouter.getInstance().build(RouterPath.MINE_FRAGMENT).navigation() as Fragment
        }
        return ARouter.getInstance().build(RouterPath.HOME_FRAGMENT).navigation() as Fragment
    }
}