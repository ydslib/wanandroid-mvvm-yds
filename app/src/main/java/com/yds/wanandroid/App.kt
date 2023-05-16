package com.yds.wanandroid

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.net.RetrofitClient
import com.yds.core.AppInitTool

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        RetrofitClient.setup("https://www.wanandroid.com", arrayListOf())
        ARouter.init(this)
        AppInitTool.initAllModuleSdk()
    }
}