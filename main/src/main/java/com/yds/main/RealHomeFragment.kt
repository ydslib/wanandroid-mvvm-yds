package com.yds.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crystallake.base.config.DataBindingConfig
import com.yds.base.BaseDataBindingFragment
import com.yds.main.adapter.RealHomeAdapter
import com.yds.main.data.RealHomeData
import com.yds.main.databinding.FragmentRealHomeBinding
import com.yds.main.vm.RealMainViewModel

class RealHomeFragment : BaseDataBindingFragment<FragmentRealHomeBinding, RealMainViewModel>() {

    val adapter by lazy {
        RealHomeAdapter()
    }

    override fun createObserver() {
        mViewModel.realHomeLiveData.observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.recyclerView?.let {
            it.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            it.adapter = adapter
        }
    }

    override fun lazyLoadData() {
        mViewModel.getAppEntryData(requireContext())
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_real_home)
    }
}