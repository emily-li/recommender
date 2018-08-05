package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Matrix;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuccessiveCollaborativeRecommenderProviderTest {
    private static SuccessiveCollaborativeRecommenderProvider recommenderProvider;
    private static SuccessiveCollaborativeRecommender recommender;
    private static Inventory inventory;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new Inventory(new Item("fooItem"), new Item("barItem"), new Item("someItem"), new Item("otherItem"));
        final double[][] data = new double[inventory.getInventory().length][inventory.getInventory().length];
        final DataSet dataSet = new DataSet(inventory.getIds(), new Matrix(data));
        recommender = new SuccessiveCollaborativeRecommender(dataSet);
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

    @Test
    public void testSuccessivePurchaseLikelihoodIncreases() throws Exception {
        final double previousLikelihood = getLikelihood(recommender, inventory.get(1).getId(), inventory.get(0).getId());
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(0).getId());

        assert getLikelihood(recommender, inventory.get(1).getId(), inventory.get(0).getId()) > previousLikelihood;
    }

    @Test
    public void testNotSuccessivePurchaseLikelihoodDecreases() throws Exception {
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(0).getId());
        final double previousLikelihood = getLikelihood(recommender, inventory.get(1).getId(), inventory.get(0).getId());
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(2).getId());
        final double currLikelihood = getLikelihood(recommender, inventory.get(1).getId(), inventory.get(0).getId());

        assert currLikelihood < previousLikelihood;
    }

    @Test
    public void testRecentPurchaseLikelihoodIncreases() throws Exception {
        final double previousLikelihood0 = getLikelihood(recommender, inventory.get(2).getId(), inventory.get(0).getId());
        final double previousLikelihood1 = getLikelihood(recommender, inventory.get(2).getId(), inventory.get(1).getId());

        recommender.registerSuccessiveItem(inventory.get(2).getId(), inventory.get(1).getId(), inventory.get(0).getId());

        assert getLikelihood(recommender, inventory.get(2).getId(), inventory.get(0).getId()) > previousLikelihood0;
        assert getLikelihood(recommender, inventory.get(2).getId(), inventory.get(1).getId()) > previousLikelihood1;
    }

    @Test(expected = RecommenderException.class)
    public void testExceptionThrownWhenUserHistoryContainsInvalidSuccessiveItem() throws Exception {
        final UserHistory userHistory = new UserHistory(inventory.get(0).getId(), "invalidItem");
        final SuccessiveCollaborativeRecommenderProvider provider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistory);
        provider.getRecommender();
    }

    private double getLikelihood(ItemBasedRecommender recommender, String item, String previousItem) throws NoSuchFieldException {
        return recommender.getDataSet().get(item, previousItem);
    }
}
