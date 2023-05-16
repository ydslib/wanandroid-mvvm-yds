package com.yds.home

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
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
        homeAdapter.addItem(BannerItem(this))
    }

    override fun lazyLoadData() {
        mViewModel.getHomeArticle(0)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_home)
    }

    override fun initOtherVM() {
        super.initOtherVM()
        mViewModel.homeArticleLiveData.observe(this) {
            it?.datas?.forEach {
                homeAdapter.addItem(HomeCarItem(it))
            }
        }
    }

    private fun setItemDecoration(){
        val divider = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )
        ContextCompat.getDrawable(requireContext(),R.drawable.item_divider)?.let {
            divider.setDrawable(it)
        }
    }
}