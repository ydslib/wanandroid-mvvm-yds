/**
 * Created by : yds
 * Time: 2023-08-10 14:55
 */
package com.yds.featured.chain;

public class OneInterceptor implements Interceptor {

    @Override
    public void intercept(Chain chain) {
        String res = chain.process("OneInterceptor");
        System.out.println("OneInterceptor:" + res);
    }

}
