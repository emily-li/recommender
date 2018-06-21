package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.UserHistory;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuccessiveBasedRecommenderProviderTest {
    private static SuccessiveBasedRecommenderProvider recommenderService;
    private static Inventory inventory;

    @BeforeClass
    public static void setupBeforeClass() {
        recommenderService = new SuccessiveBasedRecommenderProvider();
        inventory = new Inventory("firstItem", "secondItem", "thirdItem");
    }

    @Test
    public void testGetRecommenderGivenUserHistory() throws Exception {
        UserHistory userHistory = new UserHistory(inventory.getInventory()[0], inventory.getInventory()[1]);
        SuccessiveBasedRecommender recommender = recommenderService.getRecommender(inventory, userHistory);

        double expectedGreaterLikelihood = recommender.getLikelihood(inventory.getInventory()[1], inventory.getInventory()[0]);
        double expectedLowerLikelihood1 = recommender.getLikelihood(inventory.getInventory()[2], inventory.getInventory()[0]);
        double expectedLowerLikelihood2 = recommender.getLikelihood(inventory.getInventory()[0], inventory.getInventory()[2]);

        assert expectedGreaterLikelihood > expectedLowerLikelihood1;
        assert expectedGreaterLikelihood > expectedLowerLikelihood2;
    }

    @Test
    public void testGetRecommenderGivenUserHistories() throws Exception {
        UserHistory userHistory1 = new UserHistory(inventory.getInventory()[0], inventory.getInventory()[1]);
        UserHistory userHistory2 = new UserHistory(inventory.getInventory()[0], inventory.getInventory()[1], inventory.getInventory()[2]);

        SuccessiveBasedRecommender recommender = recommenderService.getRecommender(inventory, userHistory1, userHistory2);

        double expectedGreaterLikelihood1 = recommender.getLikelihood(inventory.getInventory()[1], inventory.getInventory()[0]);
        double expectedGreaterLikelihood2 = recommender.getLikelihood(inventory.getInventory()[2], inventory.getInventory()[1]);
        double expectedLowerLikelihood = recommender.getLikelihood(inventory.getInventory()[0], inventory.getInventory()[1]);

        assert expectedGreaterLikelihood1 > expectedGreaterLikelihood2;
        assert expectedGreaterLikelihood2 > expectedLowerLikelihood;
    }
}
