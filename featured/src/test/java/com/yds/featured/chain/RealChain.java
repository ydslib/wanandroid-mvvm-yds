/**
 * Created by : yds
 * Time: 2023-08-10 14:53
 */
package com.yds.featured.chain;

import java.util.ArrayList;
import java.util.List;

public class RealChain implements Chain {

    private List<Interceptor> list = new ArrayList<>();
    private int index = 0;

    public RealChain(int index, List<Interceptor> list) {
        this.index = index;
        this.list = list;
    }

    @Override
    public String process(String request) {
        if (index >= list.size()) {
            return "finish";
        }
        Chain chain = new RealChain(index + 1, list);
        Interceptor interceptor = list.get(index);
        interceptor.intercept(chain);
        return request;
    }
}
