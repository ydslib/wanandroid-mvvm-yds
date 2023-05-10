package com.crystallake.scan.ocr

import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean

class ScopedExecutor(val executor: Executor?) : Executor {

    val mShutDown by lazy {
        AtomicBoolean()
    }

    override fun execute(command: Runnable?) {
        if (mShutDown.get()) {
            return
        }

        executor?.execute {
            if (mShutDown.get()) return@execute
            command?.run()
        }
    }

    fun shutdown() {
        mShutDown.set(true)
    }

}