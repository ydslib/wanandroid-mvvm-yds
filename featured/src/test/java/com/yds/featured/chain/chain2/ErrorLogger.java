/**
 * Created by : yds
 * Time: 2023-08-10 15:44
 */
package com.yds.featured.chain.chain2;

public class ErrorLogger extends AbstractLogger {

    public ErrorLogger() {
        this.level = ERROR;
    }

    @Override
    protected void printLogger(String msg) {
        System.out.println("ErrorLogger::" + msg);
    }
}
