package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;

import java.util.Collection;
import java.util.Map;

public class HybridRecommenderTrainer extends RecommenderTrainer {
    private final Calculator calculator;
    private final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider;
    private final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider;

    public HybridRecommenderTrainer(final Calculator calculator,
                                    final Inventory inventory,
                                    final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider,
                                    final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider) {
        super(inventory);
        this.calculator = calculator;
        this.propertyBasedRecommenderProvider = propertyBasedRecommenderProvider;
        this.successiveCollaborativeRecommenderProvider = successiveCollaborativeRecommenderProvider;
    }

    @Override
    public HybridRecommender getTrainedRecommender(final Map<String, Collection<String>> validRecommendations,
                                                   final UserHistory[] userHistories,
                                                   final double significance,
                                                   final int retries) throws RecommenderException {
        for (int i = 0; i < retries; i++) {
            final double[] untrainedPrecisions = new double[100];
            final double[] trainedPrecisions = new double[100];

            HybridRecommender hybridRecommender = generateHybridRecommender(userHistories);
            getPrecisions(hybridRecommender, validRecommendations, untrainedPrecisions, trainedPrecisions);

            if (calculator.tTest(untrainedPrecisions, trainedPrecisions) < significance) {
                return hybridRecommender;
            }
        }
        throw new RecommenderException(String.format("Failed to generate recommender with an s.d. less than %s given %d retries", significance, retries));
    }

    private HybridRecommender generateHybridRecommender(final UserHistory[] userHistories) throws RecommenderException {
        final ItemBasedRecommender propertyRecommender = propertyBasedRecommenderProvider.getRecommender(1.0001, 1.0002);
        final SuccessiveCollaborativeRecommender successiveRecommender = successiveCollaborativeRecommenderProvider.getRecommender(1.0001, 1.0002);

        for (final UserHistory userHistory : userHistories) {
            final String[][] orders = userHistory.getOrderHistory();
            for (int i = 1; i < orders.length; i++) {
                for (int j = 0; j < orders[i].length; j++) {
                    try {
                        successiveRecommender.registerSuccessiveItem(orders[i][j], orders[i - 1]);
                    } catch (NoSuchFieldException e) {
                        throw new RecommenderException(e);
                    }
                }
            }
        }

        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(getInventory(), calculator, successiveRecommender, propertyRecommender);
        return hybridRecommenderProvider.getRecommender(1.0001, 1.0002);
    }
}
