package com.crystallake.sdklog

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class LogInvocationHandler(val logManager: IManager) : InvocationHandler {

    fun getManager(): IManager {
        return Proxy.newProxyInstance(this::class.java.classLoader, logManager.javaClass.interfaces, this) as IManager
    }

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        return method?.invoke(logManager, *(args ?: arrayOfNulls<Any>(0)))
    }


}