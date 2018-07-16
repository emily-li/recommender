package com.liemily.recommender;

import com.liemily.data.DataSet;

import java.util.Arrays;

public class SuccessiveCollaborativeRecommender extends ItemBasedRecommender {
    public SuccessiveCollaborativeRecommender(DataSet dataSet) {
        super(dataSet);
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
