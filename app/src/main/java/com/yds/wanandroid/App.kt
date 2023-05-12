package com.yds.wanandroid

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.yds.core.AppInitTool

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        AppInitTool.initAllModuleSdk()
    }
}