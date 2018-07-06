package com.liemily.recommender;

public abstract class ItemBasedRecommender {
    private DataSet dataSet;

    public ItemBasedRecommender(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    abstract double getLikelihood(String item, String comparatorItem) throws NoSuchFieldException;

    public DataSet getDataSet() {
        return dataSet;
    }
}
