package com.yds.wanandroid

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.net.RetrofitClient
import com.yds.base.performance.JankManager
import com.yds.core.AppInitTool

class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        RetrofitClient.setup("https://www.wanandroid.com", arrayListOf())
        ARouter.init(context.applicationContext as Application)
        AppInitTool.initAllModuleSdk()
        JankManager.init(context.applicationContext as Application)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}