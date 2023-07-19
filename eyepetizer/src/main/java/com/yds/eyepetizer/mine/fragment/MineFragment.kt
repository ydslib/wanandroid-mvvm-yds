package com.yds.eyepetizer.mine.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment
import com.yds.eyepetizer.R
import com.yds.eyepetizer.databinding.EyeFragmentMineBinding
import com.yds.eyepetizer.mine.vm.MineViewModel


@Route(path = RouterPath.EYEPETIZER_MINE_FRAGMENT)
class MineFragment : BaseDataBindingFragment<EyeFragmentMineBinding, MineViewModel>() {

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.eye_fragment_mine)
    }
}