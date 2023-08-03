package com.crystallake.sdklog.mmkv

import com.crystallake.sdklog.IManager
import com.crystallake.sdklog.LogFactory
import com.crystallake.sdklog.LogInvocationHandler

class MMKVFactory : LogFactory {

    override fun createManager(): IManager {
        return LogInvocationHandler(MMKVManager()).getManager()
    }

}