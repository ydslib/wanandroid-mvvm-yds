/**
 * Created by : yds
 * Time: 2023-08-10 15:41
 */
package com.yds.featured.chain.chain2;

public class InfoLogger extends AbstractLogger {

    public InfoLogger() {
        level = INFO;
    }

    @Override
    protected void printLogger(String msg) {
        System.out.println("InfoLogger::" + msg);
    }
}
