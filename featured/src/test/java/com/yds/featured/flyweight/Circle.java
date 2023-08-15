/**
 * Created by : yds
 * Time: 2023-08-10 16:18
 */
package com.yds.featured.flyweight;

public class Circle implements Shape {

    private String color;
    private int x;
    private int y;

    public Circle(String color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void draw() {
        System.out.println("draw circle in (" + x + "," + y + ")");
    }
}
