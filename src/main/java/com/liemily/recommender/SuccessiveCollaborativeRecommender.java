package com.liemily.recommender;

import com.liemily.data.Item;

import java.util.Arrays;

public class SuccessiveCollaborativeRecommender implements ItemBasedRecommender {
    private DataSet dataSet;

    public SuccessiveCollaborativeRecommender(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public double getLikelihood(Item item, Item previousItem) throws NoSuchFieldException {
        return dataSet.get(item.getId(), previousItem.getId());
    }

    public void registerSuccessiveItem(String current, String... previous) throws NoSuchFieldException {
        for (String item : getInventory()) {
            double likelihood = dataSet.get(current, item);
            if (Arrays.asList(previous).indexOf(item) >= 0) {
                likelihood++;
            } else {
                likelihood--;
            }
            dataSet.set(current, item, likelihood);
        }
    }

    private String[] getInventory() {
        return dataSet.getHeader();
    }
}
