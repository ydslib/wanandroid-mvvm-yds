package com.yds.core

import java.util.*

object AppInitTool {

    private val loader: ServiceLoader<AppInit> by lazy {
        ServiceLoader.load(AppInit::class.java)
    }

    fun initAllModuleSdk() {
        val iterator = loader.iterator()
        while (iterator.hasNext()) {
            iterator.next().initAllModuleSdk()
        }
    }
}