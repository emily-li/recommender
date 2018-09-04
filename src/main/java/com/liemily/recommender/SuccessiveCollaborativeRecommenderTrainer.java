package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class SuccessiveCollaborativeRecommenderTrainer extends RecommenderTrainer {
    private final Calculator calculator;

    public SuccessiveCollaborativeRecommenderTrainer(final Inventory inventory,
                                                     final Calculator calculator) {
        super(inventory);
        this.calculator = calculator;
    }

    @Override
    public SuccessiveCollaborativeRecommender getTrainedRecommender(final Map<String, Collection<String>> validRecommendations, final UserHistory[] userHistories, final double significance, final int retries) throws RecommenderException {
        final double minWeight = ThreadLocalRandom.current().nextDouble(1.001, 1.05);
        final double maxWeight = ThreadLocalRandom.current().nextDouble(minWeight + 0.001, 1.2);

        for (int i = 0; i < retries; i++) {
            final double[] trainedPrecisions = new double[100];
            final double[] untrainedPrecisions = new double[100];

            final SuccessiveCollaborativeRecommenderProvider provider = new SuccessiveCollaborativeRecommenderProvider(getInventory(), calculator, userHistories);
            final SuccessiveCollaborativeRecommender recommender = provider.getRecommender(minWeight, maxWeight);
            getPrecisions(recommender, validRecommendations, untrainedPrecisions, trainedPrecisions);

            if (calculator.tTest(untrainedPrecisions, trainedPrecisions) < significance) {
                return recommender;
            }
        }
        throw new RecommenderException(String.format("Failed to generate recommender with an s.d. less than %s given %d retries", significance, retries));
    }
}
