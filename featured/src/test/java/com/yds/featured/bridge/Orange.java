/**
 * Created by : yds
 * Time: 2023-08-02 16:54
 */
package com.yds.featured.bridge;

public class Orange implements Fruit {
    @Override
    public void add(String food) {
        System.out.println("Orange " + food);
    }
}
