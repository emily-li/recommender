package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.Matrix;

import java.util.Random;

public class UntrainedRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;

    public UntrainedRecommenderProvider(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public ItemBasedRecommender getRecommender() {
        final Item[] items = inventory.getInventory();
        final double[][] randomSimilarities = new double[items.length][items.length];
        final Random random = new Random();

        for (int i = 0; i < items.length; i++) {
            randomSimilarities[i] = random.doubles(items.length, 0, 1).toArray();
        }

        final DataSet dataSet = new DataSet(inventory.getIds(), new Matrix(randomSimilarities));
        return new ItemBasedRecommender(dataSet);
    }
}
