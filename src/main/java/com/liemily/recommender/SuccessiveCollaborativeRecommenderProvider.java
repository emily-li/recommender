package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuccessiveCollaborativeRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;
    private final UserHistory[] userHistories;

    public SuccessiveCollaborativeRecommenderProvider(final Inventory inventory, final UserHistory... userHistories) {
        this.inventory = inventory;
        this.userHistories = userHistories;
    }

    public SuccessiveCollaborativeRecommender getRecommender() throws RecommenderException {
        final String[] itemIds = inventory.getIds();


        final List<String[]> orders = new ArrayList<>();
        for (final UserHistory userHistory : userHistories) {
            final String[][] orderHistory = userHistory.getOrderHistory();
            Collections.addAll(orders, orderHistory);
        }

        final DataSet dataSet = new DataSet(itemIds, new Matrix(new double[itemIds.length][itemIds.length]));
        final SuccessiveCollaborativeRecommender recommender = new SuccessiveCollaborativeRecommender(dataSet);

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
