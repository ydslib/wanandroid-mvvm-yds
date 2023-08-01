/**
 * Created by : yds
 * Time: 2023-08-01 16:41
 */
package com.yds.featured.decorator;

public class RibbonDecorator extends ChristmasTreeDecorator {

    public RibbonDecorator(Tree tree) {
        super(tree);
    }

    @Override
    public int getPrice() {
        return super.getPrice() + 20;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ",加了缎带";
    }
}
