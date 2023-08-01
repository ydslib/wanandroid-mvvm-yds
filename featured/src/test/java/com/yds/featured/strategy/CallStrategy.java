/**
 * Created by : yds
 * Time: 2023-07-31 18:16
 */
package com.yds.featured.strategy;

/**
 * 策略模式
 */
public class CallStrategy {

    IStrategy mStrategy;

    public CallStrategy(IStrategy strategy) {
        mStrategy = strategy;
    }

    public void executeStrategy() {
        mStrategy.executeStrategy();
    }
}
