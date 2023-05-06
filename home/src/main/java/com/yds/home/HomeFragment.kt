package com.yds.home

import android.os.Bundle
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.base.vm.BaseViewModel
import com.yds.home.databinding.FragmentHomeBinding

class HomeFragment : DataBindingFragment<FragmentHomeBinding, BaseViewModel>() {
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