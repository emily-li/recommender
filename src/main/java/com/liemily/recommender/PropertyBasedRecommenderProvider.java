package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.Matrix;
import com.liemily.math.VectorCalculator;

public class PropertyBasedRecommenderProvider implements RecommenderProvider {
    private final VectorCalculator vectorCalculator;
    private final Inventory inventory;

    public PropertyBasedRecommenderProvider(final VectorCalculator vectorCalculator, final Inventory inventory) {
        this.vectorCalculator = vectorCalculator;
        this.inventory = inventory;
    }

    public ItemBasedRecommender getRecommender() {
        final Item[] items = inventory.getInventory();
        final double[][] similarities = new double[items.length][items.length];

        for (int i = 0; i < items.length; i++) {
            final Item item = items[i];
            final double[] itemProps = item.getPropArray();

            for (int j = i + 1; j < items.length - i; j++) {
                final Item comparatorItem = items[j];
                similarities[i][j] = vectorCalculator.cosineSimilarity(itemProps, comparatorItem.getPropArray());
                similarities[j][i] = similarities[i][j];
            }
        }

        final DataSet dataSet = new DataSet(inventory.getIds(), new Matrix(similarities));
        return new ItemBasedRecommender(dataSet);
    }
}
