package com.liemily.recommender;

import com.liemily.data.DataSet;
import com.liemily.data.Inventory;
import com.liemily.data.UserHistory;
import com.liemily.exception.RecommenderException;

import java.util.*;

public class SuccessiveCollaborativeRecommenderProvider implements RecommenderProvider {
    private final Inventory inventory;
    private final UserHistory[] userHistories;

    public SuccessiveCollaborativeRecommenderProvider(Inventory inventory, UserHistory... userHistories) {
        this.inventory = inventory;
        this.userHistories = userHistories;
    }

    public SuccessiveCollaborativeRecommender getRecommender() throws RecommenderException {
        String[] itemIds = inventory.getIds();

        final DataSet dataSet = new DataSet(itemIds, new double[itemIds.length][itemIds.length]);
        final SuccessiveCollaborativeRecommender recommender = new SuccessiveCollaborativeRecommender(dataSet);

        Map<String, Collection<String[]>> itemSequences = new HashMap<>();
        for (UserHistory userHistory : userHistories) {
            List<String> purchaseHistory = userHistory.getPurchaseHistory();

            for (int i = 1; i <= purchaseHistory.size() - 1; i++) {
                itemSequences.computeIfAbsent(purchaseHistory.get(i), l -> new ArrayList<>());
                itemSequences.get(purchaseHistory.get(i)).add(purchaseHistory.subList(0, i).toArray(new String[i - 1]));
            }
        }

        try {
            for (Map.Entry<String, Collection<String[]>> purchaseSequences : itemSequences.entrySet()) {
                for (String[] seq : purchaseSequences.getValue()) {
                    recommender.registerSuccessiveItem(purchaseSequences.getKey(), seq);
                }
            }
        } catch (NoSuchFieldException e) {
            throw new RecommenderException(e);
        }
        return recommender;
    }
}
