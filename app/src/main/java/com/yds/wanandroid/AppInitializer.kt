package com.yds.wanandroid

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.net.RetrofitClient
import com.didichuxing.doraemonkit.DoKit
import com.tencent.mmkv.MMKV
import com.yds.base.performance.JankManager
import com.yds.core.AppInitTool

class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        MMKV.initialize(context)
        RetrofitClient.setup("https://www.wanandroid.com", arrayListOf())
        ARouter.init(context.applicationContext as Application)
        AppInitTool.initAllModuleSdk()
        JankManager.init(context.applicationContext as Application)
        DoKit.Builder(context as Application)
            .productId("751526a624698092948317a77b6b1581")
            .build()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}