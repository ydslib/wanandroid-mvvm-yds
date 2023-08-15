/**
 * Created by : yds
 * Time: 2023-08-10 15:16
 */
package com.yds.featured.chain;

public class ThreeInterceptor implements Interceptor {
    @Override
    public void intercept(Chain chain) {
        String res = chain.process("ThreeInterceptor");
        System.out.println("ThreeInterceptor:" + res);
    }
}
