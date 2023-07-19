package com.yds.home.fjs

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment
import com.yds.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.yds.core.UserInfoTool
import com.yds.home.BuildConfig
import com.yds.home.R
import com.yds.home.databinding.FragmentHomeBinding
import com.yds.home.fjs.item.BannerItem
import com.yds.home.fjs.item.HomeCarItem
import com.yds.home.fjs.vm.HomeFragmentViewModel

@Route(path = RouterPath.HOME_FRAGMENT)
class HomeFragment : BaseDataBindingFragment<FragmentHomeBinding, HomeFragmentViewModel>() {

    private var loginState = false

    private val homeAdapter by lazy {
        MultiDataBindingAdapter().apply {
            if (BuildConfig.DEBUG) {
                openDebug()
            }
        }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun createObserver() {
        loginStateChangeObserve {
            mViewModel.getHomeArticle(0, HomeFragmentViewModel.LOAD, requireContext())
        }

        mViewModel.showLoading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
        mViewModel.homeArticleLiveData.observe(this) {
            homeAdapter.clear()
            it.banner?.let { bannerBean ->
                homeAdapter.addItem(BannerItem(bannerBean, this))
            }
            it.articleModel?.let { article ->
                article.datas?.forEach { baseArticle ->
                    homeAdapter.addItem(HomeCarItem(baseArticle) { position, collect ->
                        if (!UserInfoTool.getLoginState()) {
                            ARouter.getInstance().build(RouterPath.LOGIN_ACTIVITY).navigation()
                            return@HomeCarItem
                        }
                        if (baseArticle.collect == false) {
                            baseArticle.id.let { id ->
                                mViewModel.collectInsideWebArticle(id)
                                baseArticle.collect = true
                            }
                        } else {
                            baseArticle.id.let { id ->
                                mViewModel.uncollectInsideWebArticle(id)
                                baseArticle.collect = false
                            }
                        }
                        homeAdapter.notifyItemChanged(position)
                    })
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

    override fun initView(savedInstanceState: Bundle?) {
        loginState = UserInfoTool.getLoginState()
        mBinding?.recyclerView?.let {
            it.layoutManager = linearLayoutManager
            it.adapter = homeAdapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!requireActivity().isDestroyed && !requireActivity().isFinishing) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            Glide.with(requireContext()).resumeRequests()
                        } else {
                            Glide.with(requireContext()).pauseRequests()
                        }
                    }
                }
            })
        }

        mBinding?.smartRefreshLayout?.setOnRefreshListener {
            mViewModel.getHomeArticle(0, HomeFragmentViewModel.REFRESH, requireContext())
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            mViewModel.getLoadMoreHomeData(mViewModel.curPage.value ?: 0, requireContext())
        }
    }

    override fun lazyLoadData() {
        mViewModel.loadAllData(requireContext(), HomeFragmentViewModel.LOAD)
    }

    fun showLoading() {
        mBinding?.loadViewPage?.isVisible = true
        showLoading(mBinding?.loadingView)

    }

    fun hideLoading() {
        mBinding?.loadViewPage?.isVisible = false
        hideLoading(mBinding?.loadingView)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_home)
    }
}