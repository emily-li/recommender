package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;

import java.util.*;

public class HybridRecommenderTrainer {
    private final Calculator calculator;
    private final Inventory inventory;
    private final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider;
    private final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider;

    public HybridRecommenderTrainer(final Calculator calculator,
                                    final Inventory inventory,
                                    final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider,
                                    final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider) {
        this.calculator = calculator;
        this.inventory = inventory;
        this.propertyBasedRecommenderProvider = propertyBasedRecommenderProvider;
        this.successiveCollaborativeRecommenderProvider = successiveCollaborativeRecommenderProvider;
    }

    public ItemBasedRecommender getTrainedRecommender(final Map<String, Collection<String>> validRecommendations,
                                                      final UserHistory[] userHistories,
                                                      final double significance,
                                                      final int retries) throws Exception {
        for (int i = 0; i < retries; i++) {
            final double[] untrainedPrecisions = new double[100];
            final double[] trainedPrecisions = new double[100];

            ItemBasedRecommender hybridRecommender = generateHybridRecommender(userHistories);
            getPrecisions(hybridRecommender, validRecommendations, untrainedPrecisions, trainedPrecisions);

            if (calculator.tTest(untrainedPrecisions, trainedPrecisions) < significance) {
                return hybridRecommender;
            }
        }
        throw new RecommenderException(String.format("Failed to generate recommender with an s.d. less than %s given %d retries", significance, retries));
    }

    private ItemBasedRecommender generateHybridRecommender(final UserHistory[] userHistories) throws Exception {
        final ItemBasedRecommender propertyRecommender = propertyBasedRecommenderProvider.getRecommender(1.0001, 1.0002);
        final SuccessiveCollaborativeRecommender successiveRecommender = successiveCollaborativeRecommenderProvider.getRecommender(1.0001, 1.0002);

        for (final UserHistory userHistory : userHistories) {
            for (String[] orders : userHistory.getOrderHistory()) {
                if (orders.length > 2) {
                    successiveRecommender.registerSuccessiveItem(orders[orders.length - 1], Arrays.copyOf(orders, orders.length - 2));
                }
            }
        }

        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, calculator, successiveRecommender, propertyRecommender);
        return hybridRecommenderProvider.getRecommender(1.0001, 1.0002);
    }

    public Map<String, Collection<String>> getValidRecommendations(final UserHistory[] userHistories) {
        final Map<String, Collection<String>> validRecommendations = new HashMap<>();
        for (final UserHistory userHistory : userHistories) {
            final String[][] orderHistory = userHistory.getOrderHistory();

            for (int i = 0; i < orderHistory.length - 1; i++) {
                final String[] order = orderHistory[i];

                for (String item : order) {
                    validRecommendations.computeIfAbsent(item, x -> new HashSet<>());
                    validRecommendations.get(item).addAll(Arrays.asList(orderHistory[i + 1]));
                }
            }
        }
        return validRecommendations;
    }

    void getPrecisions(final ItemBasedRecommender hybridRecommender,
                       final Map<String, Collection<String>> validRecommendations,
                       final double[] untrainedPrecisions,
                       final double[] trainedPrecisions) throws Exception {
        for (int j = 0; j < 100; j++) {
            double untrainedFalsePositives = 0;
            double untrainedTruePositives = 0;
            double trainedFalsePositives = 0;
            double trainedTruePositives = 0;

            final ItemBasedRecommender untrainedRecommender = generateUntrainedRecommender();

            for (final String item : inventory.getIds()) {
                final Collection<String> actualRecommendations = validRecommendations.get(item);

                final String randomRecommendation = untrainedRecommender.getRecommendation(item);
                final String trainedRecommendation = hybridRecommender.getRecommendation(item);

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
            }
            untrainedPrecisions[j] = (untrainedTruePositives / (untrainedTruePositives + untrainedFalsePositives));
            trainedPrecisions[j] = (trainedTruePositives / (trainedTruePositives + trainedFalsePositives));
        }
    }

    private ItemBasedRecommender generateUntrainedRecommender() {
        return new UntrainedRecommenderProvider(inventory).getRecommender(1.0001, 1.0002);
    }
}
