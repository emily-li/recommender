package com.liemily.generator;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.entity.UserHistory;

import java.util.Random;

public class EntityGenerator {
    private final Random random;

    public EntityGenerator(Random random) {
        this.random = random;
    }


    public Inventory generateInventory(final int numItems, final int numProps) {
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

    public UserHistory[] generateUserHistories(final int numUsers, final int historicalBound, final Inventory inventory) {
        final UserHistory[] userHistories = new UserHistory[numUsers];
        for (int i = 0; i < numUsers; i++) {
            final String[] purchases = new String[random.nextInt(historicalBound)];
            for (int j = 0; j < purchases.length; j++) {
                purchases[j] = inventory.getIds()[random.nextInt(inventory.getIds().length)];
            }

            userHistories[i] = new UserHistory(purchases);
        }
        return userHistories;
    }
}
