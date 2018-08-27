package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;

import java.util.concurrent.ThreadLocalRandom;

public class PropertyBasedRecommenderProvider implements RecommenderProvider {
    private final Calculator calculator;
    private final Inventory inventory;

    public PropertyBasedRecommenderProvider(final Calculator calculator, final Inventory inventory) {
        this.calculator = calculator;
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
                final double sim = calculator.cosineSimilarity(itemProps, comparatorItem.getPropArray());
                similarities[i][j] = Math.abs(sim * ThreadLocalRandom.current().nextDouble(weightMin, weightMax));
                similarities[j][i] = Math.abs(sim * ThreadLocalRandom.current().nextDouble(weightMin, weightMax));
            }
        }
        final DataSet dataSet = new DataSet(inventory.getIds(), new Matrix(similarities));
        return new ItemBasedRecommender(dataSet);
    }
}
