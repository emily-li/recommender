package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuccessiveCollaborativeRecommenderTest {
    private static SuccessiveCollaborativeRecommender recommender;
    private static Inventory inventory;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new Inventory(new Item("fooItem"), new Item("barItem"), new Item("someItem"), new Item("otherItem"));
        final double[][] data = new double[inventory.getInventory().length][inventory.getInventory().length];
        final DataSet dataSet = new DataSet(inventory.getIds(), data);
        recommender = new SuccessiveCollaborativeRecommender(dataSet);
    }

    @Test
    public void testSuccessivePurchaseLikelihoodIncreases() throws Exception {
        final double previousLikelihood = recommender.getLikelihood(inventory.get(1), inventory.get(0));
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(0).getId());

        assert recommender.getLikelihood(inventory.get(1), inventory.get(0)) > previousLikelihood;
    }

    @Test
    public void testNotSuccessivePurchaseLikelihoodDecreases() throws Exception {
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(0).getId());
        final double previousLikelihood = recommender.getLikelihood(inventory.get(1), inventory.get(0));
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(2).getId());

        assert recommender.getLikelihood(inventory.get(1), inventory.get(0)) < previousLikelihood;
    }

    @Test
    public void testRecentPurchaseLikelihoodIncreases() throws Exception {
        final double previousLikelihood0 = recommender.getLikelihood(inventory.get(2), inventory.get(0));
        final double previousLikelihood1 = recommender.getLikelihood(inventory.get(2), inventory.get(1));

        recommender.registerSuccessiveItem(inventory.get(2).getId(), inventory.get(1).getId(), inventory.get(0).getId());

        assert recommender.getLikelihood(inventory.get(2), inventory.get(0)) > previousLikelihood0;
        assert recommender.getLikelihood(inventory.get(2), inventory.get(1)) > previousLikelihood1;
    }
}
