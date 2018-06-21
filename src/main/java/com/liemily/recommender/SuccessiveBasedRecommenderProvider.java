package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.UserHistory;

import java.util.List;

public class SuccessiveBasedRecommenderProvider {
    public SuccessiveBasedRecommender getRecommender(Inventory inventory, UserHistory... userHistoryCollection) throws NoSuchFieldException {
        String[] itemIds = inventory.getInventory();

        final DataSet dataSet = new DataSet(itemIds, new double[itemIds.length][itemIds.length]);
        final SuccessiveBasedRecommender recommender = new SuccessiveBasedRecommender(dataSet);
        for (UserHistory userHistory : userHistoryCollection) {
            List<String> purchaseHistory = userHistory.getPurchaseHistory();

            for (int i = 1; i <= purchaseHistory.size() - 1; i++) {
                recommender.registerSuccessiveItem(purchaseHistory.get(i), purchaseHistory.subList(0, i).toArray(new String[i - 1]));
            }
        }
        return recommender;
    }
}
