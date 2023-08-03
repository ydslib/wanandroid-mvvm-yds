/**
 * Created by : yds
 * Time: 2023-08-02 16:55
 */
package com.yds.featured.bridge;

public class Apple implements Fruit {
    @Override
    public void add(String food) {
        System.out.println("Apple " + food);
    }
}
