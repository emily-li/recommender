package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;
import com.liemily.math.VectorCalculator;

public class PropertyBasedRecommenderProvider {
    private VectorCalculator vectorCalculator;

    public PropertyBasedRecommenderProvider(VectorCalculator vectorCalculator) {
        this.vectorCalculator = vectorCalculator;
    }

    public PropertyBasedRecommender getRecommender(Inventory inventory) {
        final Item[] items = inventory.getInventory();

        final double[][] similarities = new double[items.length][items.length];

        for (int i = 0; i < items.length; i++) { // for each item
            final Item item = items[i];
            // get the cosine similarity for every other item
            for (int j = i + 1; j < items.length - i; j++) {
                final Item comparatorItem = items[j];
                similarities[i][j] = vectorCalculator.cosineSimilarity(item.getPropArray(), comparatorItem.getPropArray());
                similarities[j][i] = similarities[i][j];
            }
        }

        final DataSet dataSet = new DataSet(inventory.getIds(), similarities);
        return new PropertyBasedRecommender(dataSet);
    }
}
