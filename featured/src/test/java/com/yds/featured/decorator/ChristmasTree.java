/**
 * Created by : yds
 * Time: 2023-08-01 16:33
 */
package com.yds.featured.decorator;

public class ChristmasTree extends Tree{
    @Override
    public String getMessage() {
        return "圣诞树";
    }

    @Override
    public int getPrice() {
        return 50;
    }
}
