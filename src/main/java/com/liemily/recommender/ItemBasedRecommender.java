package com.liemily.recommender;

import com.liemily.data.Item;

public abstract class ItemBasedRecommender {
    private DataSet dataSet;

    public ItemBasedRecommender(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    abstract double getLikelihood(Item item, Item comparatorItem) throws NoSuchFieldException;

    public DataSet getDataSet() {
        return dataSet;
    }
}
