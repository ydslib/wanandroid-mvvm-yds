package com.yds.eyepetizer.discovery.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment
import com.yds.eyepetizer.R
import com.yds.eyepetizer.databinding.EyeFragmentDiscoveryBinding
import com.yds.eyepetizer.discovery.vm.DiscoveryViewModel

@Route(path = RouterPath.EYEPETIZER_DISCOVERY_FRAGMENT)
class DiscoveryFragment : BaseDataBindingFragment<EyeFragmentDiscoveryBinding, DiscoveryViewModel>() {

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.eye_fragment_discovery)
    }
}