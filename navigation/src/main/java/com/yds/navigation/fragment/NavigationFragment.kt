package com.yds.navigation.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.resources.RouterPath
import com.yds.navigation.R
import com.yds.navigation.databinding.FragmentNavigationBinding
import com.yds.navigation.item.NaviItem
import com.yds.navigation.vm.NavigationViewModel
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView


@Route(path = RouterPath.NAVIGATION_FRAGMENT)
class NavigationFragment : DataBindingFragment<FragmentNavigationBinding, NavigationViewModel>() {

    val adapter by lazy {
        MultiDataBindingAdapter()
    }
    val layoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    private var needScroll = false
    private var index = 0

    override fun createObserver() {
        mViewModel.naviData.observe(this) {
            it?.run {
                mBinding?.tabLayout?.setTabAdapter(object : TabAdapter {
                    override fun getCount(): Int {
                        return size
                    }

                    override fun getBadge(position: Int): ITabView.TabBadge? {
                        return null
                    }

                    override fun getIcon(position: Int): ITabView.TabIcon? {
                        return null
                    }

                    override fun getTitle(position: Int): ITabView.TabTitle {
                        return ITabView.TabTitle.Builder()
                            .setContent(get(position).name)
                            .build()
                    }

                    override fun getBackground(position: Int): Int {
                        return -1
                    }

                })
            }
            it?.forEach { model ->
                adapter.addItem(NaviItem(model))
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun smoothScrollToPosition(currentPosition: Int) {
        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        val lastPosition = layoutManager.findLastVisibleItemPosition()
        if (currentPosition <= firstPosition) {
            mBinding?.recycler?.smoothScrollToPosition(currentPosition)
        } else if (currentPosition <= lastPosition) {
            val top = mBinding?.recycler?.getChildAt(currentPosition - firstPosition)?.top ?: 0
            mBinding?.recycler?.smoothScrollBy(0, top)
        } else {
            mBinding?.recycler?.smoothScrollToPosition(currentPosition)
            needScroll = true
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.recycler?.layoutManager = layoutManager
        mBinding?.recycler?.adapter = adapter
        mBinding?.recycler?.setHasFixedSize(true)
        mBinding?.tabLayout?.addOnTabSelectedListener(object :
            VerticalTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabView?, position: Int) {
                index = position
                mBinding?.recycler?.stopScroll()
                smoothScrollToPosition(position)
            }

            override fun onTabReselected(tab: TabView?, position: Int) {

            }
        })

        mBinding?.recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if ((newState == RecyclerView.SCROLL_STATE_IDLE)) {
                    if (needScroll) {
                        scrollRecycler()
                    }
                    adapter.notifyDataSetChanged()
                }
                rightLinkageLeft(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (needScroll) {
                    scrollRecycler()
                }
            }
        })
    }

    private fun rightLinkageLeft(state: Int) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            val i = layoutManager.findFirstVisibleItemPosition()
            mBinding?.tabLayout?.setTabSelected(i)
            index = i
        }
    }

    private fun scrollRecycler() {
        needScroll = false
        val indexDistance = index - layoutManager.findFirstVisibleItemPosition()
        if (indexDistance > 0 && indexDistance < (mBinding?.recycler?.childCount ?: 0)) {
            val top = mBinding?.recycler?.getChildAt(indexDistance)?.top ?: 0
            mBinding?.recycler?.smoothScrollBy(0, top)
        }
    }

    override fun lazyLoadData() {
        mViewModel.getNaviTitleData()
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_navigation)
    }
}