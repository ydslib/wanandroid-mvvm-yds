/**
 * Created by : yds
 * Time: 2023-08-01 17:18
 */
package com.yds.featured.proxy.staticproxy;

public class PlayerProxy implements IPlayer {

    IPlayer mPlayer;

    public PlayerProxy() {
        mPlayer = new AliPlayer();
    }

    @Override
    public void play() {
        System.out.println("代理开始");
        mPlayer.play();
        System.out.println("代理结束");
    }
}
