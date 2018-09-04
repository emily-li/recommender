package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class SuccessiveCollaborativeRecommenderTrainer extends RecommenderTrainer {
    private final SuccessiveCollaborativeRecommenderProvider provider;
    private final Calculator calculator;

    public SuccessiveCollaborativeRecommenderTrainer(final Inventory inventory,
                                                     final Calculator calculator,
                                                     final SuccessiveCollaborativeRecommenderProvider provider) {
        super(inventory);
        this.calculator = calculator;
        this.provider = provider;
    }

    @Override
    public SuccessiveCollaborativeRecommender getTrainedRecommender(Map<String, Collection<String>> validRecommendations, UserHistory[] userHistories, double significance, int retries) throws RecommenderException {
        final double minWeight = ThreadLocalRandom.current().nextDouble(1.001, 1.05);
        final double maxWeight = ThreadLocalRandom.current().nextDouble(minWeight + 0.001, 1.2);

        for (int i = 0; i < retries; i++) {
            final double[] untrainedPrecisions = new double[100];
            final double[] trainedPrecisions = new double[100];

            SuccessiveCollaborativeRecommender recommender = provider.getRecommender(minWeight, maxWeight);
            getPrecisions(recommender, validRecommendations, untrainedPrecisions, trainedPrecisions);

            if (calculator.tTest(untrainedPrecisions, trainedPrecisions) < significance) {
                return recommender;
            }
        }
        throw new RecommenderException(String.format("Failed to generate recommender with an s.d. less than %s given %d retries", significance, retries));
    }
}
