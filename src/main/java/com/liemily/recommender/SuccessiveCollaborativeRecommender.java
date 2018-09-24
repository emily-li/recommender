package com.liemily.recommender;

import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;

import java.util.concurrent.ThreadLocalRandom;

public class SuccessiveCollaborativeRecommender extends ItemBasedRecommender {
    private final Calculator calculator;

    public SuccessiveCollaborativeRecommender(final DataSet dataSet,
                                              final Calculator calculator) {
        super(dataSet);
        this.calculator = calculator;
    }

    /**
     * Method that registers a successive item against previous items.
     *
     * The likelihood for the successive item increases as a recommendation for each previous item.
     *
     * The likelihood of other items decrease for each previous item.
     *
     * @param current  Successive item that will be more likely to purchase
     * @param previous Items in previous orders
     * @throws NoSuchFieldException
     */
    public void registerSuccessiveItem(String current, String... previous) throws RecommenderException {
        try {
            final int currItemIdx = getDataSet().getIndex(current);

            for (final String prevItem : previous) {
                final int prevItemIdx = getDataSet().getIndex(prevItem);
                final double[] prevItemLikelihoods = getDataSet().getData().getRow(prevItemIdx);

                for (int i = 0; i < prevItemLikelihoods.length; i++) {
                    double likelihood = prevItemLikelihoods[i];

                    if (i == currItemIdx) {
                        likelihood += 0.001;
                        likelihood = likelihood > 1 ? ThreadLocalRandom.current().nextDouble(0.9, 0.999) : likelihood;
                    } else {
                        likelihood -= 0.001;
                        likelihood = likelihood < 0 ? ThreadLocalRandom.current().nextDouble(0.001, 0.1) : likelihood;
                    }
                    getDataSet().getData().set(prevItemIdx, i, likelihood);
                }
            }
        } catch (NoSuchFieldException e) {
            throw new RecommenderException("Invalid item", e);
        }
    }
}
