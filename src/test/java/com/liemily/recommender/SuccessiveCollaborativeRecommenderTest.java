package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
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
        final double previousLikelihood = getLikelihood(recommender, inventory.get(1).getId(), inventory.get(0).getId());
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(0).getId());

        assert getLikelihood(recommender, inventory.get(1).getId(), inventory.get(0).getId()) > previousLikelihood;
    }

    @Test
    public void testNotSuccessivePurchaseLikelihoodDecreases() throws Exception {
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(0).getId());
        final double previousLikelihood = getLikelihood(recommender, inventory.get(1).getId(), inventory.get(0).getId());
        recommender.registerSuccessiveItem(inventory.get(1).getId(), inventory.get(2).getId());

        assert getLikelihood(recommender, inventory.get(1).getId(), inventory.get(0).getId()) < previousLikelihood;
    }

    @Test
    public void testRecentPurchaseLikelihoodIncreases() throws Exception {
        final double previousLikelihood0 = getLikelihood(recommender, inventory.get(2).getId(), inventory.get(0).getId());
        final double previousLikelihood1 = getLikelihood(recommender, inventory.get(2).getId(), inventory.get(1).getId());

        recommender.registerSuccessiveItem(inventory.get(2).getId(), inventory.get(1).getId(), inventory.get(0).getId());

        assert getLikelihood(recommender, inventory.get(2).getId(), inventory.get(0).getId()) > previousLikelihood0;
        assert getLikelihood(recommender, inventory.get(2).getId(), inventory.get(1).getId()) > previousLikelihood1;
    }

    private double getLikelihood(ItemBasedRecommender recommender, String item, String previousItem) throws NoSuchFieldException {
        return recommender.getDataSet().get(item, previousItem);
    }
}
