/**
 * Created by : yds
 * Time: 2023-08-01 16:20
 */
package com.yds.featured.factory.abstractfactory;

public class LiningFactory implements AbstractFactory {
    @Override
    public BasketBall createBasketBall() {
        return new LiningBasketBall();
    }

    @Override
    public FootBall createFootBall() {
        return new LiningFootBall();
    }
}
