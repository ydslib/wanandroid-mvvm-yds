/**
 * Created by : yds
 * Time: 2023-08-01 17:30
 */
package com.yds.featured.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler implements InvocationHandler {

    private Object mObject;

    public MyInvocationHandler(Object object) {
        mObject = object;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), mObject.getClass().getInterfaces(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("方法被调用之前，可以编写一些逻辑");
        Object o = method.invoke(mObject, args);
        System.out.println("方法被调用之后，可以编写一些逻辑");
        return o;
    }
}
