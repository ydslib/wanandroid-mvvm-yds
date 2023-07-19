package com.yds.eyepetizer

import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingActivity
import com.yds.eyepetizer.databinding.ActivityEyepetizerBinding


@Route(path = RouterPath.EYEPETIZER_ACTIVITY)
class EyepetizerActivity : BaseDataBindingActivity<ActivityEyepetizerBinding, BaseViewModel>() {

    override fun initData() {
        super.initData()
        mBinding?.bottomNavigation?.itemIconTintList = null
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_eyepetizer)
    }
}