package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Matrix;

import java.util.*;

public class SuccessiveCollaborativeRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;
    private final UserHistory[] userHistories;

    public SuccessiveCollaborativeRecommenderProvider(final Inventory inventory, final UserHistory... userHistories) {
        this.inventory = inventory;
        this.userHistories = userHistories;
    }

    public SuccessiveCollaborativeRecommender getRecommender() throws RecommenderException {
        final String[] itemIds = inventory.getIds();

        final Map<String, Collection<String[]>> itemSequences = new HashMap<>();
        for (UserHistory userHistory : userHistories) {
            List<String> purchaseHistory = userHistory.getPurchaseHistory();

            for (int i = 1; i <= purchaseHistory.size() - 1; i++) {
                itemSequences.computeIfAbsent(purchaseHistory.get(i), l -> new ArrayList<>());
                itemSequences.get(purchaseHistory.get(i)).add(purchaseHistory.subList(0, i).toArray(new String[i - 1]));
            }
        }

        final DataSet dataSet = new DataSet(itemIds, new Matrix(new double[itemIds.length][itemIds.length]));
        final SuccessiveCollaborativeRecommender recommender = new SuccessiveCollaborativeRecommender(dataSet);

        try {
            for (final Map.Entry<String, Collection<String[]>> purchaseSequences : itemSequences.entrySet()) {
                for (final String[] seq : purchaseSequences.getValue()) {
                    recommender.registerSuccessiveItem(purchaseSequences.getKey(), seq);
                }
            }
        } catch (final NoSuchFieldException e) {
            throw new RecommenderException(e);
        }
        return recommender;
    }
}
