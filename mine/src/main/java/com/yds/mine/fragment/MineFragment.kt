package com.yds.mine.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.yds.mine.R
import com.yds.mine.databinding.FragmentMineBinding

@Route(path = RouterPath.MINE_FRAGMENT)
class MineFragment : DataBindingFragment<FragmentMineBinding, BaseViewModel>() {

    override fun createObserver() {
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_mine)
    }
}