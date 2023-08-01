package com.yds.eyepetizer.home.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.RequestMethod
import com.crystallake.base.vm.State
import com.crystallake.resources.RouterPath
import com.gyf.immersionbar.ktx.immersionBar
import com.yds.base.BaseDataBindingFragment
import com.yds.eyepetizer.R
import com.yds.eyepetizer.databinding.EyeFragmentHomeBinding
import com.yds.eyepetizer.home.adapter.HomeAdapter
import com.yds.eyepetizer.home.vm.HomeFragmentViewModel

@Route(path = RouterPath.EYEPETIZER_HOME_FRAGMENT)
class HomeFragment : BaseDataBindingFragment<EyeFragmentHomeBinding, HomeFragmentViewModel>() {

    private val homeAdapter by lazy {
        HomeAdapter(requireActivity())
    }

    override fun createObserver() {
        mViewModel.homeBannerLiveData.observe(this) {
            homeAdapter.setList(mutableListOf())
            homeAdapter.addData(it)
        }
        mViewModel.homeDailyLiveData.observe(this) {
            if (mViewModel.mStateLiveData.value == State.RefreshState) {

            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarColor(R.color.white)
            applySystemFits(true)
        }
        mBinding?.recyclerView?.adapter = homeAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        mBinding?.recyclerView?.layoutManager = layoutManager
    }

    override fun lazyLoadData() {
        mViewModel?.getHomeBanner(RequestMethod.Loading)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.eye_fragment_home)
    }
}