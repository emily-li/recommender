package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;

public class HybridRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;
    private final ItemBasedRecommender[] recommenders;
    private final Calculator calculator;

    public HybridRecommenderProvider(final Inventory inventory, final ItemBasedRecommender[] recommenders, final Calculator calculator) {
        this.inventory = inventory;
        this.recommenders = recommenders;
        this.calculator = calculator;
    }

    @Override
    public ItemBasedRecommender getRecommender(double weightMin, double weightMax) {
        final DataSet dataSet = combineDatasets();
        return new ItemBasedRecommender(dataSet);
    }

    private DataSet combineDatasets() {
        Matrix probabilities = null;
        for (final ItemBasedRecommender recommender : recommenders) {
            final DataSet dataSet = recommender.getDataSet();
            probabilities = probabilities == null ? dataSet.getData() : calculator.add(probabilities, dataSet.getData());
        }
        return new DataSet(inventory.getIds(), probabilities);
    }
}
