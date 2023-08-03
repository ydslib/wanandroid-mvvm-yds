package com.crystallake.sdklog.sp

import com.crystallake.sdklog.IManager
import com.crystallake.sdklog.LogFactory
import com.crystallake.sdklog.LogInvocationHandler

class SpFractory : LogFactory {
    override fun createManager(): IManager {
        return LogInvocationHandler(SharedPreferenceManager()).getManager()
    }
}