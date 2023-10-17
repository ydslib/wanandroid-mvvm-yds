package com.crystallake.appfunctiontest

import com.crystallake.appfunctiontest.databinding.ActivityAppFuncTestBinding
import com.crystallake.appfunctiontest.vm.AppFuncTestViewModel
import com.crystallake.base.config.DataBindingConfig
import com.yds.base.BaseDataBindingActivity

class AppFuncTest : BaseDataBindingActivity<ActivityAppFuncTestBinding, AppFuncTestViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_app_func_test)
    }

    override fun initData() {
        super.initData()
        mBinding?.diff?.setOnClickListener {
            mViewModel.fileDiff()
        }

        mBinding?.patch?.setOnClickListener {
            mViewModel.filePatch()
        }
    }

}