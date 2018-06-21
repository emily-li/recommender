package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.UserHistory;

import java.util.*;

public class SuccessiveBasedRecommenderProvider {
    public SuccessiveBasedRecommender getRecommender(Inventory inventory, UserHistory... userHistoryCollection) throws NoSuchFieldException {
        String[] itemIds = inventory.getInventory();

        final DataSet dataSet = new DataSet(itemIds, new double[itemIds.length][itemIds.length]);
        final SuccessiveBasedRecommender recommender = new SuccessiveBasedRecommender(dataSet);

        Map<String, Collection<String[]>> itemSequences = new HashMap<>();
        for (UserHistory userHistory : userHistoryCollection) {
            List<String> purchaseHistory = userHistory.getPurchaseHistory();

            for (int i = 1; i <= purchaseHistory.size() - 1; i++) {
                itemSequences.computeIfAbsent(purchaseHistory.get(i), l -> new ArrayList<>());
                itemSequences.get(purchaseHistory.get(i)).add(purchaseHistory.subList(0, i).toArray(new String[i - 1]));
            }
        }

        for (Map.Entry<String, Collection<String[]>> purchaseSequences : itemSequences.entrySet()) {
            for (String[] seq : purchaseSequences.getValue()) {
                recommender.registerSuccessiveItem(purchaseSequences.getKey(), seq);
            }
        }
        return recommender;
    }
}
