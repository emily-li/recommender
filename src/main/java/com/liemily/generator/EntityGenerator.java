package com.liemily.generator;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;

import java.util.Random;

public class EntityGenerator {
    public Inventory generate(final int numItems, final int numProps) {
        Random random = new Random();

        final double[][] propBin = new double[random.nextInt(numProps)][numProps];

        for (int i = 0; i < propBin.length; i++) {
            final int numPopulatedProps = random.nextInt(numProps);
            for (int j = 0; j < numPopulatedProps; j++) {
                propBin[i][random.nextInt(numPopulatedProps - j)] = Math.random();
            }
        }

        final Item[] items = new Item[numItems];
        for (int i = 0; i < numItems; i++) {
            final Item item = new Item("item" + i, propBin[random.nextInt(propBin.length)]);

            items[i] = item;
        }
        return new Inventory(items);
    }
}
