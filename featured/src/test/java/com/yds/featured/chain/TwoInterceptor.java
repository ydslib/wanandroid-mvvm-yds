/**
 * Created by : yds
 * Time: 2023-08-10 14:56
 */
package com.yds.featured.chain;

public class TwoInterceptor implements Interceptor {

    @Override
    public void intercept(Chain chain) {
        String res = chain.process("TwoInterceptor");
        System.out.println("TwoInterceptor:" + res);
    }
}
