package com.yds.login

import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.yds.login.databinding.ActivityLoginBinding


@Route(path = RouterPath.LOGIN_ACTIVITY)
class LoginActivity : DataBindingActivity<ActivityLoginBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_login)
    }


}