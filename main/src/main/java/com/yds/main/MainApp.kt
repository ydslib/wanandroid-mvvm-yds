package com.yds.main

import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.app.BaseApplication
import com.tencent.mmkv.MMKV
import com.yds.core.AppInit

class MainApp : BaseApplication(), AppInit {

    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        MMKV.initialize(this)
        ARouter.init(this)
    }

    override fun initAllModuleSdk() {

    }
}