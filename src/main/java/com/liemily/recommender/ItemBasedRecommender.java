package com.liemily.recommender;

public class ItemBasedRecommender {
    private final DataSet dataSet;

    public ItemBasedRecommender(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public String getRecommendation(String item) throws NoSuchFieldException {
        final double[] probabilities = dataSet.get(item);

        double max = -1;
        int maxIdx = -1;
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > max) {
                max = probabilities[i];
                maxIdx = i;
            }
        }
        return dataSet.getHeader()[maxIdx];
    }

    public DataSet getDataSet() {
        return dataSet;
    }

}
