package com.yds.main

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SPUtils
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.net.RetrofitClient
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingActivity
import com.yds.base.countDown
import com.yds.core.UserInfoTool
import com.yds.main.databinding.ActivityWelcomeBinding

class WelcomeActivity : BaseDataBindingActivity<ActivityWelcomeBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_welcome)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val isFirstEnter = SPUtils.getInstance("CommonParams").getBoolean("isFirstEnter", true)
        if (!isFirstEnter) {
            jumpToMain()
        }
        super.onCreate(savedInstanceState)
    }

    override fun initData() {
        super.initData()
        initImmersionBar(R.color.transparent)
        if (RetrofitClient.hasCookie()) {
            val loginState = SPUtils.getInstance("UserInfo").getBoolean("loginState")
            val userName = SPUtils.getInstance("UserInfo").getString("username")
            UserInfoTool.setLoginState(loginState)
            UserInfoTool.setUserName(userName)
        }

        countDown(5,
            next = {
                mBinding?.countDown?.text = it.toString()
            }, end = {
                SPUtils.getInstance("CommonParams").put("isFirstEnter", false)
                jumpToMain()
            })

    }

    fun jumpToMain() {
        ARouter.getInstance().build(RouterPath.MAIN_ACTIVITY).navigation()
        finish()
    }
}