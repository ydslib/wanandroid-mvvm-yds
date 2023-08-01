/**
 * Created by : yds
 * Time: 2023-08-01 17:18
 */
package com.yds.featured.proxy.staticproxy;

public class AliPlayer implements IPlayer{
    @Override
    public void play() {
        System.out.println("ali 播放器");
    }
}
