package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.VectorCalculator;

public class PropertyBasedRecommenderProvider implements RecommenderProvider {
    private final VectorCalculator vectorCalculator;
    private final Inventory inventory;

    public PropertyBasedRecommenderProvider(VectorCalculator vectorCalculator, Inventory inventory) {
        this.vectorCalculator = vectorCalculator;
        this.inventory = inventory;
    }

    public ItemBasedRecommender getRecommender() {
        final Item[] items = inventory.getInventory();

        final double[][] similarities = new double[items.length][items.length];

        for (int i = 0; i < items.length; i++) {
            final Item item = items[i];
            for (int j = i + 1; j < items.length - i; j++) {
                final Item comparatorItem = items[j];
                similarities[i][j] = vectorCalculator.cosineSimilarity(item.getPropArray(), comparatorItem.getPropArray()); // TODO: Take this out of double for, optimise
                similarities[j][i] = similarities[i][j];
            }
        }

        final DataSet dataSet = new DataSet(inventory.getIds(), similarities);
        return new ItemBasedRecommender(dataSet);
    }
}
