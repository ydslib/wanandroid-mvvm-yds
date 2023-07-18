package com.yds.home

import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.app.BaseApplication
import com.crystallake.base.net.RetrofitClient
import com.yds.core.AppInit

class HomeApp : BaseApplication(), AppInit {

    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        RetrofitClient.setup("https://www.wanandroid.com", arrayListOf())
    }

    override fun initAllModuleSdk() {

    }
}