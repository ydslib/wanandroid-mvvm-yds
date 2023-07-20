package com.yds.eyepetizer.home.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.RequestMethod
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment
import com.yds.eyepetizer.R
import com.yds.eyepetizer.databinding.EyeFragmentHomeBinding
import com.yds.eyepetizer.home.vm.HomeFragmentViewModel

@Route(path = RouterPath.EYEPETIZER_HOME_FRAGMENT)
class HomeFragment : BaseDataBindingFragment<EyeFragmentHomeBinding, HomeFragmentViewModel>() {

    override fun createObserver() {
        mViewModel.homeBannerLiveData.observe(this) {
            println("$it")
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {
        mViewModel?.getHomeBanner(RequestMethod.Loading)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.eye_fragment_home)
    }
}