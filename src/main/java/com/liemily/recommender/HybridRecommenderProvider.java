package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.math.MatrixCalculator;

public class HybridRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;
    private final ItemBasedRecommender recommenders[];
    private final MatrixCalculator matrixCalculator;

    public HybridRecommenderProvider(Inventory inventory, ItemBasedRecommender[] recommenders, MatrixCalculator matrixCalculator) {
        this.inventory = inventory;
        this.recommenders = recommenders;
        this.matrixCalculator = matrixCalculator;
    }

    @Override
    public ItemBasedRecommender getRecommender() {
        DataSet dataSet = combineDatasets();
        return new ItemBasedRecommender(dataSet);
    }

    private DataSet combineDatasets() {
        double[][] probabilities = null;
        for (ItemBasedRecommender recommender : recommenders) {
            DataSet dataSet = recommender.getDataSet();
            probabilities = probabilities == null ? dataSet.getData() : matrixCalculator.multiply(probabilities, dataSet.getData());
        }
        return new DataSet(inventory.getIds(), probabilities);
    }
}
