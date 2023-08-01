/**
 * Created by : yds
 * Time: 2023-08-01 16:21
 */
package com.yds.featured.factory.abstractfactory;

public class AntaFactory implements AbstractFactory {

    @Override
    public BasketBall createBasketBall() {
        return new AntaBasketBall();
    }

    @Override
    public FootBall createFootBall() {
        return new AntaFootBall();
    }
}
