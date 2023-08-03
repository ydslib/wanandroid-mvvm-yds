package com.crystallake.sdklog.db

import com.crystallake.sdklog.IManager
import com.crystallake.sdklog.LogFactory
import com.crystallake.sdklog.LogInvocationHandler

class LogRoomFactory : LogFactory {
    override fun createManager(): IManager {
        return LogInvocationHandler(LogRoomManager()).getManager()
    }
}