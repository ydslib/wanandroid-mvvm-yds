/**
 * Created by : yds
 * Time: 2023-08-01 15:43
 */
package com.yds.featured.factory.simplefactory;

/**
 * 简单工厂模式
 */
public class BProduct implements IProduct{
    @Override
    public void doSomething() {
        System.out.println("BProduct doSomething");
    }
}
