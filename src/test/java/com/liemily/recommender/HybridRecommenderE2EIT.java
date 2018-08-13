package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.InventoryService;
import com.liemily.entity.UserHistory;
import com.liemily.entity.UserHistoryService;
import com.liemily.math.MatrixCalculator;
import com.liemily.math.VectorCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 * Created by Emily Li on 03/08/2018.
 */
public class HybridRecommenderE2EIT {
    private static Inventory inventory;
    private static ItemBasedRecommender untrainedRecommender;
    private static ItemBasedRecommender hybridRecommender;

    private static UserHistory[] userHistories;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        final InventoryService inventoryService = new InventoryService();
        inventory = inventoryService.getInventory("src/test/resources/testInventoryData.txt");

        final UserHistoryService userHistoryService = new UserHistoryService();
        userHistories = userHistoryService.getUserHistories("src/test/resources/testUserHistoryData.txt");

        final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(new VectorCalculator(), inventory);
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());

        final ItemBasedRecommender propertyRecommender = propertyBasedRecommenderProvider.getRecommender();
        final ItemBasedRecommender successiveRecommender = successiveCollaborativeRecommenderProvider.getRecommender();

        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, new ItemBasedRecommender[]{propertyRecommender, successiveRecommender}, new MatrixCalculator());
        hybridRecommender = hybridRecommenderProvider.getRecommender();

        untrainedRecommender = new UntrainedRecommenderProvider(inventory).getRecommender();
    }

    @Test
    public void testHybridRecommenderPrecisionHigherThanRandom() throws Exception {
        final Map<String, Collection<String>> validRecommendations = getValidRecommendations(userHistories);
        final double[] untrainedPrecisions = new double[100];
        final double[] trainedPrecisions = new double[100];

        for (int i = 0; i < 1; i++) {
            double untrainedFalsePositives = 0;
            double untrainedTruePositives = 0;
            double trainedFalsePositives = 0;
            double trainedTruePositives = 0;

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

            untrainedPrecisions[i] = (untrainedTruePositives / (untrainedTruePositives + untrainedFalsePositives));
            trainedPrecisions[i] = (trainedTruePositives / (trainedTruePositives + trainedFalsePositives));
        }

        assert tTest(untrainedPrecisions, trainedPrecisions) < 0.05;
    }

    private double tTest(final double[] x, final double[] y) {
        final double xMean = Arrays.stream(x).average().orElse(0);
        final double yMean = Arrays.stream(y).average().orElse(0);

        final double xVar = getVariance(x);
        final double yVar = getVariance(y);

        final double xDiv = xVar == 0 ? 0 : xVar / x.length;
        final double yDiv = yVar == 0 ? 0 : yVar / y.length;

        final double meanDiff = xMean - yMean;
        final double divSqrt = (xDiv + yDiv) == 0 ? 0 : Math.sqrt(xDiv + yDiv);
        return meanDiff / divSqrt;
    }

    private double getVariance(double[] doubles) {
        final double mean = Arrays.stream(doubles).average().orElse(0);

        double tmp = 0;
        for (final double d : doubles) {
            tmp += Math.sqrt(d - mean);
        }
        return tmp == 0 ? 0 : tmp / (doubles.length - 1);
    }

    private Map<String, Collection<String>> getValidRecommendations(final UserHistory[] userHistories) {
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
}
