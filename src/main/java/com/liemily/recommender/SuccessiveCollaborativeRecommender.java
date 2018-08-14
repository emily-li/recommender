package com.liemily.recommender;

import java.util.Arrays;

public class SuccessiveCollaborativeRecommender extends ItemBasedRecommender {
    public SuccessiveCollaborativeRecommender(DataSet dataSet) {
        super(dataSet);
    }

    public void registerSuccessiveItem(String current, String... previous) throws NoSuchFieldException {
        final String[] header = getDataSet().getHeader();
        final int currentItemIdx = getDataSet().getIndex(current);

        for (int i = 0; i < header.length; i++) {
            final String item = header[i];

            double likelihood = getDataSet().get(current, item);
            if (Arrays.asList(previous).indexOf(item) >= 0) {
                likelihood++;
            } else {
                likelihood--;
            }
            likelihood = sigmoid(likelihood);
            getDataSet().getData().set(currentItemIdx, i, likelihood);
        }
    }
}
