/**
 * Created by : yds
 * Time: 2023-08-01 17:29
 */
package com.yds.featured.proxy.dynamicproxy;

public class HouseOwner implements Rent {
    @Override
    public void rent() {
        System.out.println("房东出租房子！");
    }
}
