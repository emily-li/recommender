package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.InventoryService;
import com.liemily.entity.UserHistory;
import com.liemily.entity.UserHistoryService;
import com.liemily.math.Calculator;
import org.junit.Test;

import java.util.*;

public class HybridRecommenderTrainerIT {
    @Test
    public void testHybridRecommenderPrecisionRealDataE2E() throws Exception {
        final Calculator calculator = new Calculator();
        final InventoryService inventoryService = new InventoryService();
        final Inventory inventory = inventoryService.getInventory("src/test/resources/hybridE2EInventory.txt");

        final UserHistoryService userHistoryService = new UserHistoryService();
        final UserHistory[] userHistories = userHistoryService.getUserHistories("src/test/resources/hybridE2EUserHistories.txt");

        final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(calculator, inventory);
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());
        final HybridRecommenderTrainer hybridRecommenderTrainer = new HybridRecommenderTrainer(calculator, inventory, propertyBasedRecommenderProvider, successiveCollaborativeRecommenderProvider);

        final Map<String, Collection<String>> validRecommendations = getValidRecommendations(userHistories);

        final ItemBasedRecommender hybridRecommender = hybridRecommenderTrainer.getTrainedRecommender(validRecommendations, userHistories, 0.05, 10);

        final double[] untrainedPrecisions = new double[100];
        final double[] trainedPrecisions = new double[100];
        hybridRecommenderTrainer.getPrecisions(hybridRecommender, validRecommendations, untrainedPrecisions, trainedPrecisions);
        assert calculator.tTest(untrainedPrecisions, trainedPrecisions) < 0.05;
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
