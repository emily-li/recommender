package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;

import java.util.concurrent.ThreadLocalRandom;

public class SuccessiveCollaborativeRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;
    private final UserHistory[] userHistories;
    private final Calculator calculator;

    public SuccessiveCollaborativeRecommenderProvider(final Inventory inventory,
                                                      final Calculator calculator,
                                                      final UserHistory... userHistories) {
        this.inventory = inventory;
        this.userHistories = userHistories;
        this.calculator = calculator;
    }

    @Override
    public SuccessiveCollaborativeRecommender getRecommender(double weightMin, double weightMax) throws RecommenderException {
        final String[] itemIds = inventory.getIds();

        final double[][] weightedDataSet = new double[inventory.getIds().length][inventory.getIds().length];
        for (int i = 0; i < inventory.getIds().length; i++) {
            for (int j = 0; j < inventory.getIds().length; j++) {
                weightedDataSet[i][j] = calculator.sigmoid(ThreadLocalRandom.current().nextDouble(weightMin, weightMax));
            }
        }

        final DataSet dataSet = new DataSet(itemIds, new Matrix(weightedDataSet));
        final SuccessiveCollaborativeRecommender recommender = new SuccessiveCollaborativeRecommender(dataSet, calculator);

        registerSuccessiveOrders(recommender);
        return recommender;
    }

    public void registerSuccessiveOrders(final SuccessiveCollaborativeRecommender successiveCollaborativeRecommender) throws RecommenderException {
        for (UserHistory userHistory : userHistories) {
            String[][] orders = userHistory.getOrderHistory();
            for (int i = 1; i < orders.length; i++) {
                for (int j = 0; j < orders[i].length; j++) {
                    for (int k = 0; k < i; k++) {
                        successiveCollaborativeRecommender.registerSuccessiveItem(orders[i][j], orders[k]);
                    }
                }
            }
        }
    }
}
