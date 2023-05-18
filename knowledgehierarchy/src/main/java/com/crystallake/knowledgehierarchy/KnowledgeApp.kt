package com.crystallake.knowledgehierarchy

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.yds.core.AppInit

class KnowledgeApp : Application(),AppInit {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }

    override fun initAllModuleSdk() {

    }
}