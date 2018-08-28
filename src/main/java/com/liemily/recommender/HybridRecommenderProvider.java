package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;

public class HybridRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;
    private SuccessiveCollaborativeRecommender successiveCollaborativeRecommender;
    private ItemBasedRecommender auxiliaryRecommender;
    private final Calculator calculator;

    public HybridRecommenderProvider(final Inventory inventory,
                                     final Calculator calculator,
                                     final SuccessiveCollaborativeRecommender successiveCollaborativeRecommender,
                                     final ItemBasedRecommender auxiliaryRecommender) {
        this.inventory = inventory;
        this.calculator = calculator;
        this.successiveCollaborativeRecommender = successiveCollaborativeRecommender;
        this.auxiliaryRecommender = auxiliaryRecommender;
    }

    @Override
    public HybridRecommender getRecommender(double weightMin, double weightMax) {
        final DataSet dataSet = combineDatasets();
        return new HybridRecommender(dataSet, successiveCollaborativeRecommender, new ItemBasedRecommender[]{auxiliaryRecommender});
    }

    private DataSet combineDatasets() {
        Matrix probabilities = successiveCollaborativeRecommender.getDataSet().getData();

        for (final ItemBasedRecommender recommender : new ItemBasedRecommender[]{auxiliaryRecommender}) {
            final DataSet dataSet = recommender.getDataSet();
            probabilities = calculator.add(probabilities, dataSet.getData());
        }

        return new DataSet(inventory.getIds(), probabilities);
    }
}
