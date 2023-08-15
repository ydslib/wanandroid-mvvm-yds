/**
 * Created by : yds
 * Time: 2023-08-10 16:21
 */
package com.yds.featured.flyweight;

import java.util.HashMap;
import java.util.Map;

public class ShapeFactory {

    private static Map<String, Circle> map = new HashMap<>();

    public static Shape getShape(String color) {
        Circle circle = map.get(color);
        resetShapeAttr(circle);
        if (circle == null) {
            circle = new Circle(color);
            map.put(color, circle);
        }
        return circle;
    }

    private static void resetShapeAttr(Circle circle) {
        circle.setX(0);
        circle.setY(0);
    }

}
