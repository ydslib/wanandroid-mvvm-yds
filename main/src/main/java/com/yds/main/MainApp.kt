package com.yds.main

import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.app.BaseApplication
import com.yds.core.AppInit

class MainApp : BaseApplication(), AppInit {

    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }

    override fun initAllModuleSdk() {

    }
}