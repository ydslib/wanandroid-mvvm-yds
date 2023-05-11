package com.yds.home

import android.os.Bundle
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.yds.home.databinding.FragmentHomeBinding
import com.yds.home.vm.HomeFragmentViewModel

class HomeFragment : DataBindingFragment<FragmentHomeBinding, HomeFragmentViewModel>() {
    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_home)
    }
}