package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.InventoryService;
import com.liemily.entity.UserHistory;
import com.liemily.entity.UserHistoryService;
import com.liemily.math.Calculator;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class HybridRecommenderTrainerIT {

    @Test
    public void testHybridRecommenderPrecisionRealDataSubSet() throws Exception {
        final Calculator calculator = new Calculator();
        final InventoryService inventoryService = new InventoryService();
        final Inventory inventory = inventoryService.getInventory("src/test/resources/hybridSubsetInventory.txt");

        final UserHistoryService userHistoryService = new UserHistoryService();
        final UserHistory[] userHistories = userHistoryService.getUserHistories("src/test/resources/hybridSubsetUserHistories.txt");

        final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(calculator, inventory);
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new Calculator(), userHistories);

        final HybridRecommenderTrainer hybridRecommenderTrainer = new HybridRecommenderTrainer(calculator, inventory, propertyBasedRecommenderProvider, successiveCollaborativeRecommenderProvider);
        final Map<String, Collection<String>> validRecommendations = hybridRecommenderTrainer.getValidRecommendations(userHistories);
        final ItemBasedRecommender hybridRecommender = hybridRecommenderTrainer.getTrainedRecommender(validRecommendations, userHistories, 0.05, 10);

        final double[] untrainedPrecisions = new double[100];
        final double[] trainedPrecisions = new double[100];
        hybridRecommenderTrainer.getPrecisions(hybridRecommender, validRecommendations, untrainedPrecisions, trainedPrecisions);
        assert calculator.tTest(untrainedPrecisions, trainedPrecisions) < 0.05;
    }

    @Test
    public void testHybridRecommenderPrecisionRealDataE2E() throws Exception {
        final Calculator calculator = new Calculator();
        final InventoryService inventoryService = new InventoryService();
        final Inventory inventory = inventoryService.getInventory("src/test/resources/hybridE2EInventory.txt");

        final UserHistoryService userHistoryService = new UserHistoryService();
        final UserHistory[] userHistories = userHistoryService.getUserHistories("src/test/resources/hybridE2EUserHistories.txt");

        final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(calculator, inventory);
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new Calculator(), userHistories);
        final HybridRecommenderTrainer hybridRecommenderTrainer = new HybridRecommenderTrainer(calculator, inventory, propertyBasedRecommenderProvider, successiveCollaborativeRecommenderProvider);

        final Map<String, Collection<String>> validRecommendations = hybridRecommenderTrainer.getValidRecommendations(userHistories);

        final ItemBasedRecommender hybridRecommender = hybridRecommenderTrainer.getTrainedRecommender(validRecommendations, userHistories, 0.05, 10);

        final double[] untrainedPrecisions = new double[100];
        final double[] trainedPrecisions = new double[100];
        hybridRecommenderTrainer.getPrecisions(hybridRecommender, validRecommendations, untrainedPrecisions, trainedPrecisions);
        assert calculator.tTest(untrainedPrecisions, trainedPrecisions) < 0.05;
    }
}
