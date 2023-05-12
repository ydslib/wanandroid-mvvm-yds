package com.yds.home

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.yds.core.AppInit

class HomeApp : Application(), AppInit {

    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }

    override fun initAllModuleSdk() {

    }
}