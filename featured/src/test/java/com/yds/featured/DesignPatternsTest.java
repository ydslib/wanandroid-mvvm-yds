/**
 * Created by : yds
 * Time: 2023-07-31 18:10
 */
package com.yds.featured;

import com.yds.featured.decorator.BallDecorator;
import com.yds.featured.decorator.ChristmasTree;
import com.yds.featured.decorator.ChristmasTreeDecorator;
import com.yds.featured.decorator.RibbonDecorator;
import com.yds.featured.decorator.Tree;
import com.yds.featured.factory.abstractfactory.AbstractFactory;
import com.yds.featured.factory.abstractfactory.AntaFactory;
import com.yds.featured.factory.abstractfactory.BasketBall;
import com.yds.featured.factory.abstractfactory.FootBall;
import com.yds.featured.factory.factorymethod.FactoryA;
import com.yds.featured.factory.factorymethod.IFactory;
import com.yds.featured.factory.simplefactory.IProduct;
import com.yds.featured.factory.simplefactory.SimpleFactory;
import com.yds.featured.proxy.dynamicproxy.HouseOwner;
import com.yds.featured.proxy.dynamicproxy.MyInvocationHandler;
import com.yds.featured.proxy.dynamicproxy.Rent;
import com.yds.featured.proxy.staticproxy.IPlayer;
import com.yds.featured.proxy.staticproxy.PlayerProxy;
import com.yds.featured.strategy.CacheStrategy;
import com.yds.featured.strategy.CallStrategy;
import com.yds.featured.strategy.IStrategy;
import com.yds.featured.strategy.NetworkStrategy;

import org.junit.Test;

public class DesignPatternsTest {

    @Test
    public void testDesignPatterns() {
        testDynamicProxy();
    }

    /**
     * 动态代理模式
     */
    public void testDynamicProxy() {
        Rent houseOwner = new HouseOwner();
        MyInvocationHandler invocationHandler = new MyInvocationHandler(houseOwner);
        Rent rent = (Rent) invocationHandler.getProxy();
        rent.rent();
    }

    /**
     * 静态代理
     */
    public void testStaticProxy() {
        IPlayer player = new PlayerProxy();
        player.play();
    }

    /**
     * 装饰器模式
     */
    public void testDecorator() {
        Tree tree = new ChristmasTree();
        //圣诞树装饰器
        ChristmasTreeDecorator decorator = new ChristmasTreeDecorator(tree);
        System.out.println(decorator.getMessage() + decorator.getPrice());
        //装饰球
        BallDecorator ballDecorator = new BallDecorator(decorator);
        System.out.println(ballDecorator.getMessage() + ballDecorator.getPrice());
        //缎带装饰器
        RibbonDecorator ribbonDecorator = new RibbonDecorator(ballDecorator);
        System.out.println(ribbonDecorator.getMessage() + ribbonDecorator.getPrice());
    }

    /**
     * 抽象工厂模式
     */
    public void testAbstractFactory() {
        //Anta
        AbstractFactory factory = new AntaFactory();
        BasketBall basketBall = factory.createBasketBall();
        basketBall.brand();

        FootBall footBall = factory.createFootBall();
        footBall.brand();
    }


    /**
     * 工厂方法模式
     */
    public void testFactoryMethod() {
        IFactory factory = new FactoryA();
        IProduct product = factory.createProduct();
        product.doSomething();
    }


    /**
     * 简单工厂模式
     */
    public void testSimpleFactory() {
        SimpleFactory factory = new SimpleFactory();
        IProduct product = factory.createProduct("ProductB");
        product.doSomething();
    }

    /**
     * 策略模式
     */
    public void testStrategy() {
        CacheStrategy cacheStrategy = new CacheStrategy();
        executeStrategy(cacheStrategy);

        NetworkStrategy networkStrategy = new NetworkStrategy();
        executeStrategy(networkStrategy);
    }

    public void executeStrategy(IStrategy strategy) {
        CallStrategy callStrategy = new CallStrategy(strategy);
        callStrategy.executeStrategy();
    }
}
