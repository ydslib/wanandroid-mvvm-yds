package com.crystallake.knowledgehierarchy

import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.app.BaseApplication
import com.yds.core.AppInit

class KnowledgeApp : BaseApplication(),AppInit {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }

    override fun initAllModuleSdk() {

    }
}