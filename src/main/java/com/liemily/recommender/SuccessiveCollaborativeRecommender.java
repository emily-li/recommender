package com.liemily.recommender;

import com.liemily.data.Item;

import java.util.Arrays;

public class SuccessiveCollaborativeRecommender extends ItemBasedRecommender {
    public SuccessiveCollaborativeRecommender(DataSet dataSet) {
        super(dataSet);
    }

    @Override
    public double getLikelihood(Item item, Item previousItem) throws NoSuchFieldException {
        return getDataSet().get(item.getId(), previousItem.getId());
    }

    public void registerSuccessiveItem(String current, String... previous) throws NoSuchFieldException {
        for (String item : getDataSet().getHeader()) {
            double likelihood = getDataSet().get(current, item);
            if (Arrays.asList(previous).indexOf(item) >= 0) {
                likelihood++;
            } else {
                likelihood--;
            }
            getDataSet().set(current, item, likelihood);
        }
    }
}
