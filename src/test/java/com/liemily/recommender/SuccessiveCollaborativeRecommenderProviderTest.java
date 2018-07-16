package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.entity.UserHistory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuccessiveCollaborativeRecommenderProviderTest {
    private static SuccessiveCollaborativeRecommenderProvider recommenderProvider;
    private static Inventory inventory;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new Inventory(new Item("firstItem"), new Item("secondItem"), new Item("thirdItem"));
    }

    @Test
    public void testGetRecommenderGivenUserHistory() throws Exception {
        UserHistory userHistory = new UserHistory(inventory.get(0).getId(), inventory.get(1).getId());

        recommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistory);
        SuccessiveCollaborativeRecommender recommender = recommenderProvider.getRecommender();

        String recForSecondItem = recommender.getRecommendation(inventory.get(1).getId());

        Assert.assertEquals(inventory.get(0).getId(), recForSecondItem);

    }

    @Test
    public void testGetRecommenderGivenUserHistories() throws Exception {
        UserHistory userHistory1 = new UserHistory(inventory.get(0).getId(), inventory.get(2).getId());
        UserHistory userHistory2 = new UserHistory(inventory.get(0).getId(), inventory.get(1).getId(), inventory.get(2).getId());

        recommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistory1, userHistory2);
        SuccessiveCollaborativeRecommender recommender = recommenderProvider.getRecommender();

        String recForThirdItem = recommender.getRecommendation(inventory.get(2).getId());

        Assert.assertEquals(inventory.get(0).getId(), recForThirdItem);
    }
}
