package com.yds.main

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.net.RetrofitClient
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.tencent.mmkv.MMKV
import com.yds.base.BaseDataBindingActivity
import com.yds.base.countDown
import com.yds.core.UserInfoTool
import com.yds.main.databinding.ActivityWelcomeBinding

class WelcomeActivity : BaseDataBindingActivity<ActivityWelcomeBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_welcome)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val isFirstEnter = MMKV.defaultMMKV().decodeBool("isFirstEnter", true)
        if (!isFirstEnter) {
            jumpToMain()
        }
        super.onCreate(savedInstanceState)
    }

    override fun initData() {
        super.initData()
        initImmersionBar(R.color.transparent)
        if (RetrofitClient.hasCookie()) {
            val loginState = MMKV.defaultMMKV().decodeBool("loginState", false)
            val userName = MMKV.defaultMMKV().decodeString("username", "") ?: ""
            UserInfoTool.setLoginState(loginState)
            UserInfoTool.setUserName(userName)
        }

        countDown(3,
            next = {
                mBinding?.countDown?.text = it.toString()
            }, end = {
                MMKV.defaultMMKV().encode("isFirstEnter", false)
                jumpToMain()
            })

    }

    fun jumpToMain() {
        ARouter.getInstance().build(RouterPath.MAIN_ACTIVITY).navigation()
        finish()
    }
}