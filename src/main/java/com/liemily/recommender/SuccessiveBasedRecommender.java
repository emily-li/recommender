package com.liemily.recommender;

import java.util.Arrays;

public class SuccessiveBasedRecommender {
    private DataSet dataSet;

    public SuccessiveBasedRecommender(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void registerSuccessiveItem(String current, String... previous) throws NoSuchFieldException {
        for (String item : getItems()) {
            double likelihood = dataSet.get(current, item);
            if (Arrays.asList(previous).indexOf(item) >= 0) {
                likelihood++;
            } else {
                likelihood--;
            }
            dataSet.set(current, item, likelihood);
        }
    }

    public double getLikelihood(String item, String previousItem) throws NoSuchFieldException {
        return dataSet.get(item, previousItem);
    }

    private String[] getItems() {
        return dataSet.getHeader();
    }
}
