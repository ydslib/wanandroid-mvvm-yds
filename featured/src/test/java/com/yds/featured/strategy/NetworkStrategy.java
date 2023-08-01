/**
 * Created by : yds
 * Time: 2023-07-31 18:16
 */
package com.yds.featured.strategy;

/**
 * 策略模式
 */
public class NetworkStrategy implements IStrategy {

    @Override
    public void executeStrategy() {
        System.out.println("NetworkStrategy");
    }
}
