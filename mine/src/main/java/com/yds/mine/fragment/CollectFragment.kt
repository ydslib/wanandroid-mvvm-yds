package com.yds.mine.fragment

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.crystallake.base.vm.BaseViewModel
import com.yds.base.BaseDataBindingFragment
import com.yds.mine.CollectItem
import com.yds.mine.R
import com.yds.mine.databinding.FragmentCollectBinding
import com.yds.mine.vm.MineViewModel

class CollectFragment : BaseDataBindingFragment<FragmentCollectBinding, MineViewModel>() {

    val adapter by lazy {
        MultiDataBindingAdapter()
    }

    private val mActivityViewModel: MineViewModel? by lazy {
        getActivityScopeViewModel(MineViewModel::class.java)
    }

    val linearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    override fun createObserver() {
        mActivityViewModel?.collectLiveData?.observe(this) {
            it.datas?.forEach {
                adapter.addItem(CollectItem(it))
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.recyclerView?.let {
            it.adapter = adapter
            it.layoutManager = linearLayoutManager
        }
    }

    override fun lazyLoadData() {
        mActivityViewModel?.getCollectList(0)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_collect)
    }
}