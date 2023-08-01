/**
 * Created by : yds
 * Time: 2023-08-01 15:56
 */
package com.yds.featured.factory.factorymethod;

import com.yds.featured.factory.simplefactory.IProduct;

public class FactoryA implements IFactory{
    @Override
    public IProduct createProduct() {
        return new ProductA();
    }
}
