package com.yds.home

import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.yds.home.databinding.ActivityHomeBinding

class HomeActivity : DataBindingActivity<ActivityHomeBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_home)
    }
}