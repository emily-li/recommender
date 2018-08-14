package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.InventoryService;
import com.liemily.entity.UserHistory;
import com.liemily.entity.UserHistoryService;
import com.liemily.generator.EntityGenerator;
import com.liemily.math.MatrixCalculator;
import com.liemily.math.VectorCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Emily Li on 03/08/2018.
 */
public class HybridRecommenderE2EIT {
    private static MatrixCalculator matrixCalculator;
    private static VectorCalculator vectorCalculator;

    private Inventory inventory;
    private UserHistory[] userHistories;
    private PropertyBasedRecommenderProvider propertyBasedRecommenderProvider;
    private SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider;

    private ItemBasedRecommender hybridRecommender;
    private ItemBasedRecommender untrainedRecommender;

    @BeforeClass
    public static void setupBeforeClass() {
        matrixCalculator = new MatrixCalculator();
        vectorCalculator = new VectorCalculator();
    }

    @Test
    public void testHybridRecommenderPrecisionTestData() throws Exception {
        final EntityGenerator entityGenerator = new EntityGenerator();
        inventory = entityGenerator.generateInventory(100, 20);
        userHistories = entityGenerator.generateUserHistories(1000, 5, inventory);

        propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(vectorCalculator, inventory);
        successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistories);

        hybridRecommender = new HybridRecommenderProvider(inventory,
                new ItemBasedRecommender[]{
                        propertyBasedRecommenderProvider.getRecommender(1.0001, 1.0002),
                        successiveCollaborativeRecommenderProvider.getRecommender(1.0001, 1.0002)
                },
                matrixCalculator)
                .getRecommender(1.00001, 1.0002);

        final Collection<UserHistory> userHistoriesWithMultOrders = new ArrayList<>();
        for (final UserHistory userHistory : userHistories) {
            if (userHistory.getOrderHistory().length > 1) {
                userHistoriesWithMultOrders.add(userHistory);
            }
        }

        int truePositives = 0;
        for (final UserHistory userHistory : userHistoriesWithMultOrders) {
            final String[][] orders = userHistory.getOrderHistory();
            final int randOrderIdx = ThreadLocalRandom.current().nextInt(1, orders.length);
            final String[] randOrder = orders[randOrderIdx];

            final String recommendation = hybridRecommender.getRecommendation(randOrder[ThreadLocalRandom.current().nextInt(0, randOrder.length)]);
            for (int i = 0; i < randOrderIdx; i++) {
                if (Arrays.asList(orders[i]).contains(recommendation)) {
                    truePositives++;
                }
            }
        }

        assert truePositives > 0;
    }

    @Test
    public void testHybridRecommenderPrecisionRealDataE2E() throws Exception {
        final InventoryService inventoryService = new InventoryService();
        inventory = inventoryService.getInventory("src/test/resources/hybridE2EInventory.txt");

        final UserHistoryService userHistoryService = new UserHistoryService();
        userHistories = userHistoryService.getUserHistories("src/test/resources/hybridE2EUserHistories.txt");

        propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(vectorCalculator, inventory);
        successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());


        final Map<String, Collection<String>> validRecommendations = getValidRecommendations(userHistories);
        final double[] untrainedPrecisions = new double[100];
        final double[] trainedPrecisions = new double[100];

        for (int i = 0; i < 100; i++) {
            double untrainedFalsePositives = 0;
            double untrainedTruePositives = 0;
            double trainedFalsePositives = 0;
            double trainedTruePositives = 0;

            regenerateRecommenders();

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

    private void regenerateRecommenders() throws Exception {
        final ItemBasedRecommender propertyRecommender = propertyBasedRecommenderProvider.getRecommender(1.0001, 1.0002);
        final SuccessiveCollaborativeRecommender successiveRecommender = successiveCollaborativeRecommenderProvider.getRecommender(1.0001, 1.0002);

        for (final UserHistory userHistory : userHistories) {
            for (String[] orders : userHistory.getOrderHistory()) {
                if (orders.length > 2) {
                    successiveRecommender.registerSuccessiveItem(orders[orders.length - 1], Arrays.copyOf(orders, orders.length - 2));
                }
            }
        }

        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(
                inventory,
                new ItemBasedRecommender[]{propertyRecommender, successiveRecommender},
                matrixCalculator);
        hybridRecommender = hybridRecommenderProvider.getRecommender(1.0001, 1.0002);

        untrainedRecommender = new UntrainedRecommenderProvider(inventory).getRecommender(1.0001, 1.0002);
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
            tmp += Math.sqrt(Math.abs(d - mean));
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
