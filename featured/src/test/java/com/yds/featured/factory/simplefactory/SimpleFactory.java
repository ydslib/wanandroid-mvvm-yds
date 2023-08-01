/**
 * Created by : yds
 * Time: 2023-08-01 15:45
 */
package com.yds.featured.factory.simplefactory;

/**
 * 简单工厂模式
 */
public class SimpleFactory {

    public IProduct createProduct(String productName) {
        if (productName.equals("ProductA")) {
            return new AProduct();
        } else if (productName.equals("ProductB")) {
            return new BProduct();
        } else {
            return null;
        }
    }
}
