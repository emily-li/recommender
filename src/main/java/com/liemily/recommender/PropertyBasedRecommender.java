package com.liemily.recommender;

public class PropertyBasedRecommender extends ItemBasedRecommender {
    public PropertyBasedRecommender(DataSet dataSet) {
        super(dataSet);
    }

    @Override
    public double getLikelihood(String item, String comparatorItem) throws NoSuchFieldException {
        final int itemIdx = getDataSet().getIndex(item);
        final int cItemIdx = getDataSet().getIndex(item);
        return getDataSet().getData()[itemIdx][cItemIdx];
    }
}
