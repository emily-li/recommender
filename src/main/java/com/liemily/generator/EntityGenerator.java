package com.liemily.generator;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.entity.UserHistory;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class EntityGenerator {
    public Inventory generateInventory(final int numItems, final int numProps) {
        final double[][] propBin = new double[ThreadLocalRandom.current().nextInt(1, numProps)][numProps];

        for (int i = 0; i < propBin.length; i++) {
            final int numPopulatedProps = ThreadLocalRandom.current().nextInt(numProps);
            for (int j = 0; j < numPopulatedProps; j++) {
                propBin[i][ThreadLocalRandom.current().nextInt(numPopulatedProps - j)] = Math.random();
            }
        }

        final Item[] items = new Item[numItems];
        for (int i = 0; i < numItems; i++) {
            final Item item = new Item("item" + i, propBin[ThreadLocalRandom.current().nextInt(propBin.length)]);

            items[i] = item;
        }
        return new Inventory(items);
    }

    public UserHistory[] generateUserHistories(final int numUsers, final int historicalBound, final Inventory inventory) {
        final UserHistory[] userHistories = new UserHistory[numUsers];
        for (int i = 0; i < numUsers; i++) {
            final String[] purchases = new String[ThreadLocalRandom.current().nextInt(historicalBound)];
            for (int j = 0; j < purchases.length; j++) {
                purchases[j] = inventory.getIds()[ThreadLocalRandom.current().nextInt(inventory.getIds().length)];
            }

            userHistories[i] = new UserHistory(Arrays.stream(purchases).map(p -> new String[]{p}).toArray(String[][]::new));
        }
        return userHistories;
    }
}
