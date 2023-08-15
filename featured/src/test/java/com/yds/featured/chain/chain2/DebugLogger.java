/**
 * Created by : yds
 * Time: 2023-08-10 15:43
 */
package com.yds.featured.chain.chain2;

public class DebugLogger extends AbstractLogger {

    public DebugLogger() {
        this.level = DEBUG;
    }

    @Override
    protected void printLogger(String msg) {
        System.out.println("DebugLogger::" + msg);
    }
}
