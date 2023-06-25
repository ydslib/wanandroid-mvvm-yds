package com.yds.home.db

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ArticleExecutors() {

    val mDiskIO: Executor
    val mNetworkIO: Executor
    val mMainThread: Executor

    init {
        mDiskIO = Executors.newSingleThreadExecutor()
        mNetworkIO = Executors.newFixedThreadPool(3)
        mMainThread = MainThreadExecutor()
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler by lazy {
            Handler(Looper.getMainLooper())
        }

        override fun execute(command: Runnable?) {
            command?.let {
                mainThreadHandler.post(it)
            }
        }
    }
}