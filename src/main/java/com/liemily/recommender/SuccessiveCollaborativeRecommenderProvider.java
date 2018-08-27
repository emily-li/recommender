package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SuccessiveCollaborativeRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;
    private final UserHistory[] userHistories;

    public SuccessiveCollaborativeRecommenderProvider(final Inventory inventory, final UserHistory... userHistories) {
        this.inventory = inventory;
        this.userHistories = userHistories;
    }

    @Override
    public SuccessiveCollaborativeRecommender getRecommender(double weightMin, double weightMax) throws RecommenderException {
        final Calculator calculator = new Calculator();
        final String[] itemIds = inventory.getIds();

        final double[][] weightedDataSet = new double[inventory.getIds().length][inventory.getIds().length];
        for (int i = 0; i < inventory.getIds().length; i++) {
            for (int j = 0; j < inventory.getIds().length; j++) {
                weightedDataSet[i][j] = calculator.sigmoid(ThreadLocalRandom.current().nextDouble(weightMin, weightMax));
            }
        }

        final DataSet dataSet = new DataSet(itemIds, new Matrix(weightedDataSet));
        final SuccessiveCollaborativeRecommender recommender = new SuccessiveCollaborativeRecommender(dataSet, new Calculator());

        final List<String[]> orders = new ArrayList<>();
        for (final UserHistory userHistory : userHistories) {
            final String[][] orderHistory = userHistory.getOrderHistory();
            Collections.addAll(orders, orderHistory);
        }
        try {
            for (int i = 0; i < orders.size() - 1; i++) {
                for (int j = 0; j < orders.get(i).length; j++) {
                    recommender.registerSuccessiveItem(orders.get(i)[j], orders.get(i + 1));
                }
            }
        } catch (final NoSuchFieldException e) {
            throw new RecommenderException(e);
        }
        return recommender;
    }
}
