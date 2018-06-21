package com.liemily.recommender;

import com.liemily.data.Item;

public class PropertyBasedRecommender implements ItemBasedRecommender {
    private DataSet dataSet;

    public PropertyBasedRecommender(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public double getLikelihood(Item item, Item comparatorItem) {
        return 0;
    }
}
