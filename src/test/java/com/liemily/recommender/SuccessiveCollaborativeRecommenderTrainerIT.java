package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.InventoryService;
import com.liemily.entity.UserHistory;
import com.liemily.entity.UserHistoryService;
import com.liemily.math.Calculator;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class SuccessiveCollaborativeRecommenderTrainerIT {
    private static Calculator calculator = new Calculator();

    @Test
    public void testOnRealSubset() throws Exception {
        final InventoryService inventoryService = new InventoryService();
        final Inventory inventory = inventoryService.getInventory("src/test/resources/hybridSubsetInventory.txt");

        final UserHistoryService userHistoryService = new UserHistoryService();
        final UserHistory[] userHistories = userHistoryService.getUserHistories("src/test/resources/hybridSubsetUserHistories.txt");

        final SuccessiveCollaborativeRecommenderProvider provider = new SuccessiveCollaborativeRecommenderProvider(inventory, calculator, userHistories);
        final SuccessiveCollaborativeRecommenderTrainer trainer = new SuccessiveCollaborativeRecommenderTrainer(inventory, calculator, provider);
        final Map<String, Collection<String>> validRecommendations = trainer.getValidRecommendations(userHistories);
        final ItemBasedRecommender recommender = trainer.getTrainedRecommender(validRecommendations, userHistories, 0.5, 10);

        final double[] untrainedPrecisions = new double[100];
        final double[] trainedPrecisions = new double[100];
        trainer.getPrecisions(recommender, validRecommendations, untrainedPrecisions, trainedPrecisions);

        assert calculator.tTest(untrainedPrecisions, trainedPrecisions) < 0.05;
    }
}