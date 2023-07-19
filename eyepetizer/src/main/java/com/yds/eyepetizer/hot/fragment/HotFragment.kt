package com.yds.eyepetizer.hot.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingFragment
import com.yds.eyepetizer.R
import com.yds.eyepetizer.databinding.EyeFragmentHotBinding
import com.yds.eyepetizer.hot.vm.HotViewModel

@Route(path = RouterPath.EYEPETIZER_HOT_FRAGMENT)
class HotFragment : BaseDataBindingFragment<EyeFragmentHotBinding, HotViewModel>() {

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.eye_fragment_hot)
    }
}