package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.Matrix;
import com.liemily.math.VectorCalculator;

import java.util.concurrent.ThreadLocalRandom;

public class PropertyBasedRecommenderProvider implements RecommenderProvider {
    private final VectorCalculator vectorCalculator;
    private final Inventory inventory;

    public PropertyBasedRecommenderProvider(final VectorCalculator vectorCalculator, final Inventory inventory) {
        this.vectorCalculator = vectorCalculator;
        this.inventory = inventory;
    }

    @Override
    public ItemBasedRecommender getRecommender(double weightMin, double weightMax) {
        final Item[] items = inventory.getInventory();
        final double[][] similarities = new double[items.length][items.length];

        for (int i = 0; i < items.length; i++) {
            final Item item = items[i];
            final double[] itemProps = item.getPropArray();

            for (int j = i + 1; j < items.length - i; j++) {
                final Item comparatorItem = items[j];
                final double sim = vectorCalculator.cosineSimilarity(itemProps, comparatorItem.getPropArray());
                similarities[i][j] = sim * ThreadLocalRandom.current().nextDouble(weightMin, weightMax);
                similarities[j][i] = sim * ThreadLocalRandom.current().nextDouble(weightMin, weightMax);
            }
        }

        final DataSet dataSet = new DataSet(inventory.getIds(), new Matrix(similarities));
        return new ItemBasedRecommender(dataSet);
    }
}
