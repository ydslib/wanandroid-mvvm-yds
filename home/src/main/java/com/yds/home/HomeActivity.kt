package com.yds.home

import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.yds.base.BaseDataBindingActivity
import com.yds.home.databinding.ActivityHomeBinding
import com.yds.home.fjs.HomeFragment

class HomeActivity : BaseDataBindingActivity<ActivityHomeBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_home)
    }

    override fun initData() {
        super.initData()
        initImmersionBar(R.color.color_333333)
        setSupportActionBar(mBinding?.homeToolbar)

        mBinding?.homeToolbar?.setNavigationOnClickListener {
            println("点击了返回")
        }
        supportActionBar?.title = ""

        supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment()).commit()
    }


}