/**
 * Created by : yds
 * Time: 2023-08-01 15:55
 */
package com.yds.featured.factory.factorymethod;

import com.yds.featured.factory.simplefactory.IProduct;

public class ProductB implements IProduct {

    @Override
    public void doSomething() {
        System.out.println("ProductB doSomething");
    }
}
