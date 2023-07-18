package com.yds.main

import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingActivity
import com.yds.main.databinding.ActivityRealMainBinding
import com.yds.main.vm.RealMainViewModel

@Route(path = RouterPath.MAIN_REAL_MAIN_ACTIVITY)
class RealMainActivity : BaseDataBindingActivity<ActivityRealMainBinding, RealMainViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_real_main)
    }

    override fun initData() {
        super.initData()
        supportFragmentManager.beginTransaction().replace(R.id.container, RealHomeFragment()).commit()
    }
}