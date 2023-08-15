/**
 * Created by : yds
 * Time: 2023-08-10 15:36
 */
package com.yds.featured.chain.chain2;

public abstract class AbstractLogger {
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int ERROR = 3;

    protected int level;

    protected AbstractLogger next;

    public void setNextLogger(AbstractLogger next) {
        this.next = next;
    }

    public void printLogger(int level, String msg) {
        if (this.level == level) {
            printLogger(msg);
        }
        if (next != null) {
            next.printLogger(level, msg);
        }
    }

    protected abstract void printLogger(String msg);
}
