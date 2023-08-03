/**
 * Created by : yds
 * Time: 2023-08-02 16:49
 */
package com.yds.featured.bridge;

public abstract class Food {

    Fruit mFruit;

    public void setFruit(Fruit fruit){
        mFruit = fruit;
    }

    public abstract void add();

}
