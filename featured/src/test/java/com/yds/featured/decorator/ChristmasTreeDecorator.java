/**
 * Created by : yds
 * Time: 2023-08-01 16:39
 */
package com.yds.featured.decorator;

public class ChristmasTreeDecorator extends Tree {

    private Tree mTree;

    public ChristmasTreeDecorator(Tree tree) {
        mTree = tree;
    }

    @Override
    public String getMessage() {
        return mTree.getMessage();
    }

    @Override
    public int getPrice() {
        return mTree.getPrice();
    }
}
