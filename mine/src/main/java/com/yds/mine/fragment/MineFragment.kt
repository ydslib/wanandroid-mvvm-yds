package com.yds.mine.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.yds.mine.R
import com.yds.mine.databinding.FragmentMineBinding
import com.yds.mine.vm.MineViewModel

@Route(path = RouterPath.MINE_FRAGMENT)
class MineFragment : DataBindingFragment<FragmentMineBinding, MineViewModel>() {

    override fun createObserver() {
        mViewModel.coinLiveData.observe(this) {
            mBinding?.username?.text = it?.userName
            mBinding?.rankNum?.text = it?.rank?.toString()
            mBinding?.coinNum?.text = it?.coinCount?.toString()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun lazyLoadData() {
        mViewModel.getCoin()
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_mine)
    }
}