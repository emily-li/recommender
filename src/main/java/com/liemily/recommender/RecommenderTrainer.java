package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;

import java.util.*;

public abstract class RecommenderTrainer {
    private final Inventory inventory;

    public RecommenderTrainer(Inventory inventory) {
        this.inventory = inventory;
    }

    public abstract ItemBasedRecommender getTrainedRecommender(final Map<String, Collection<String>> validRecommendations,
                                                               final UserHistory[] userHistories,
                                                               final double significance,
                                                               final int retries) throws RecommenderException;

    public Map<String, Collection<String>> getValidRecommendations(final UserHistory... userHistories) {
        final Map<String, Collection<String>> validRecommendations = new HashMap<>();
        for (final UserHistory userHistory : userHistories) {
            final String[][] orderHistory = userHistory.getOrderHistory();

            for (int i = 0; i < orderHistory.length - 1; i++) {
                final String[] order = orderHistory[i];

                for (String item : order) {
                    validRecommendations.computeIfAbsent(item, x -> new HashSet<>());
                    for (int j = i + 1; j < orderHistory.length; j++)
                        validRecommendations.get(item).addAll(Arrays.asList(orderHistory[j]));
                }
            }
        }
        return validRecommendations;
    }

    void getPrecisions(final ItemBasedRecommender recommender,
                       final Map<String, Collection<String>> validRecommendations,
                       final double[] untrainedPrecisions,
                       final double[] trainedPrecisions) throws RecommenderException {
        for (int j = 0; j < 100; j++) {
            double untrainedFalsePositives = 0;
            double untrainedTruePositives = 0;
            double trainedFalsePositives = 0;
            double trainedTruePositives = 0;

            final ItemBasedRecommender untrainedRecommender = new UntrainedRecommenderProvider(inventory).getRecommender(Math.random(), 1);

            for (final String item : inventory.getIds()) {
                try {
                    final Collection<String> actualRecommendations = validRecommendations.get(item);

                    final String randomRecommendation = untrainedRecommender.getRecommendation(item);
                    final String trainedRecommendation = recommender.getRecommendation(item);

                    if ((actualRecommendations == null && randomRecommendation == null) || (actualRecommendations != null && actualRecommendations.contains(randomRecommendation))) {
                        untrainedTruePositives++;
                    } else {
                        untrainedFalsePositives++;
                    }

                    if ((actualRecommendations == null && trainedRecommendation == null) || (actualRecommendations != null && actualRecommendations.contains(trainedRecommendation))) {
                        trainedTruePositives++;
                    } else {
                        trainedFalsePositives++;
                    }
                } catch (NoSuchFieldException e) {
                    throw new RecommenderException("Invalid item", e);
                }
            }
            untrainedPrecisions[j] = (untrainedTruePositives / (untrainedTruePositives + untrainedFalsePositives));
            trainedPrecisions[j] = (trainedTruePositives / (trainedTruePositives + trainedFalsePositives));
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}
