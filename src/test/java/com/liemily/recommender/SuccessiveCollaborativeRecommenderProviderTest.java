package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;
import com.liemily.data.UserHistory;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuccessiveCollaborativeRecommenderProviderTest {
    private static SuccessiveBasedRecommenderProvider recommenderService;
    private static Inventory inventory;

    @BeforeClass
    public static void setupBeforeClass() {
        recommenderService = new SuccessiveBasedRecommenderProvider();
        inventory = new Inventory(new Item("firstItem"), new Item("secondItem"), new Item("thirdItem"));
    }

    @Test
    public void testGetRecommenderGivenUserHistory() throws Exception {
        UserHistory userHistory = new UserHistory(inventory.get(0).getId(), inventory.get(1).getId());
        SuccessiveCollaborativeRecommender recommender = recommenderService.getRecommender(inventory, userHistory);

        double expectedGreaterLikelihood = recommender.getLikelihood(inventory.get(1), inventory.get(0));
        double expectedLowerLikelihood1 = recommender.getLikelihood(inventory.get(2), inventory.get(0));
        double expectedLowerLikelihood2 = recommender.getLikelihood(inventory.get(0), inventory.get(2));

        assert expectedGreaterLikelihood > expectedLowerLikelihood1;
        assert expectedGreaterLikelihood > expectedLowerLikelihood2;
    }

    @Test
    public void testGetRecommenderGivenUserHistories() throws Exception {
        UserHistory userHistory1 = new UserHistory(inventory.get(0).getId(), inventory.get(1).getId());
        UserHistory userHistory2 = new UserHistory(inventory.get(0).getId(), inventory.get(1).getId(), inventory.get(2).getId());

        SuccessiveCollaborativeRecommender recommender = recommenderService.getRecommender(inventory, userHistory1, userHistory2);

        double expectedGreaterLikelihood1 = recommender.getLikelihood(inventory.get(1), inventory.get(0));
        double expectedGreaterLikelihood2 = recommender.getLikelihood(inventory.get(2), inventory.get(1));
        double expectedLowerLikelihood = recommender.getLikelihood(inventory.get(0), inventory.get(1));

        assert expectedGreaterLikelihood1 > expectedGreaterLikelihood2;
        assert expectedGreaterLikelihood2 > expectedLowerLikelihood;
    }
}
