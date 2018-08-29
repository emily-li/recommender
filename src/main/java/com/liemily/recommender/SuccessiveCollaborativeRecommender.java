package com.liemily.recommender;

import com.liemily.math.Calculator;

import java.util.Arrays;

public class SuccessiveCollaborativeRecommender extends ItemBasedRecommender {
    private Calculator calculator;

    public SuccessiveCollaborativeRecommender(final DataSet dataSet,
                                              final Calculator calculator) {
        super(dataSet);
        this.calculator = calculator;
    }

    public void registerSuccessiveItem(String current, String... previous) throws NoSuchFieldException {
        final String[] header = getDataSet().getHeader();
        final int currentItemIdx = getDataSet().getIndex(current);

        for (int i = 0; i < header.length; i++) {
            final String item = header[i];

            double likelihood = getDataSet().get(current, item);
            if (Arrays.asList(previous).indexOf(item) >= 0) {
                likelihood *= 1.5;
            } else {
                likelihood--;
            }
            likelihood = calculator.sigmoid(likelihood);
            getDataSet().getData().set(currentItemIdx, i, likelihood);
        }
    }
}
