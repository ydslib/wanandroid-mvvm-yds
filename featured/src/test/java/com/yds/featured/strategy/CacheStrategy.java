/**
 * Created by : yds
 * Time: 2023-07-31 18:12
 */
package com.yds.featured.strategy;

/**
 * 策略模式
 */
public class CacheStrategy implements IStrategy {

    @Override
    public void executeStrategy() {
        System.out.println("CacheStrategy");
    }
}
