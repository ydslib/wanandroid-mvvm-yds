package com.yds.home

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.resources.RouterPath
import com.yds.home.databinding.FragmentHomeBinding
import com.yds.home.item.BannerItem
import com.yds.home.item.HomeCarItem
import com.yds.home.vm.HomeFragmentViewModel

@Route(path = RouterPath.HOME_FRAGMENT)
class HomeFragment : DataBindingFragment<FragmentHomeBinding, HomeFragmentViewModel>() {

    private val homeAdapter by lazy {
        MultiDataBindingAdapter()
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.recyclerView?.let {
            it.layoutManager = linearLayoutManager
            it.adapter = homeAdapter
        }

        mBinding?.smartRefreshLayout?.setOnRefreshListener {
            mViewModel.getHomeArticle(0, 0)
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            mViewModel.getLoadMoreHomeData(mViewModel.curPage.value ?: 0)
        }
    }

    override fun lazyLoadData() {
        mViewModel.getHomeArticle(mViewModel.curPage.value ?: 0, 2)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_home)
    }

    override fun initOtherVM() {
        super.initOtherVM()
        mViewModel.homeArticleLiveData.observe(this) {
            homeAdapter.clear()
            it.banner?.let { bannerBean ->
                homeAdapter.addItem(BannerItem(bannerBean, this))
            }
            it.articleModel?.let { article ->
                article.datas?.forEach { baseArticle ->
                    homeAdapter.addItem(HomeCarItem(baseArticle))
                }
            }
            homeAdapter.notifyDataSetChanged()
        }
        mViewModel.refresh.observe(this) {
            if (!it) {
                mBinding?.smartRefreshLayout?.finishRefresh()
            }
        }
        mViewModel.loadMore.observe(this) {
            if (!it) {
                mBinding?.smartRefreshLayout?.finishLoadMore()
            }
        }
    }
}