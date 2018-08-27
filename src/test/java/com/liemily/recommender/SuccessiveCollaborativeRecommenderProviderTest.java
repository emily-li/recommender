package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class SuccessiveCollaborativeRecommenderProviderTest {
    private static SuccessiveCollaborativeRecommender recommender;
    private static Inventory inventory;
    private static DataSet dataSet;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new Inventory(new Item("fooItem"), new Item("barItem"), new Item("someItem"), new Item("otherItem"));
        final double[][] data = new double[inventory.getInventory().length][inventory.getInventory().length];
        dataSet = new DataSet(inventory.getIds(), new Matrix(data));
    }

    @Before
    public void setup() {
        recommender = new SuccessiveCollaborativeRecommender(dataSet, new Calculator());
    }

    @Test
    public void testGetRecommenderGivenUserHistory() throws Exception {
        final UserHistory userHistory = new UserHistory(new String[]{inventory.get(0).getId(), inventory.get(3).getId()}, new String[]{inventory.get(1).getId(), inventory.get(2).getId()});

        final SuccessiveCollaborativeRecommenderProvider recommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistory);
        recommender = recommenderProvider.getRecommender(1.01, 1.02);

        assert (inventory.get(2).getId().equals(recommender.getRecommendation(inventory.get(0).getId())) || inventory.get(1).getId().equals(recommender.getRecommendation(inventory.get(0).getId())));
        assert (inventory.get(2).getId().equals(recommender.getRecommendation(inventory.get(3).getId())) || inventory.get(1).getId().equals(recommender.getRecommendation(inventory.get(3).getId())));
    }

    @Test
    public void testGetRecommenderGivenUserHistories() throws Exception {
        final UserHistory userHistory1 = new UserHistory(new String[]{inventory.get(0).getId()}, new String[]{inventory.get(2).getId()});
        final UserHistory userHistory2 = new UserHistory(new String[]{inventory.get(0).getId(), inventory.get(1).getId()}, new String[]{inventory.get(2).getId()});

        final SuccessiveCollaborativeRecommenderProvider recommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistory1, userHistory2);
        recommender = recommenderProvider.getRecommender(1, 1.5);

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
    public void testExceptionThrownWhenUserHistoryContainsInvalidSucceededItem() throws Exception {
        final UserHistory userHistory = new UserHistory(new String[]{"invalidItem"}, new String[]{inventory.get(0).getId()});
        final SuccessiveCollaborativeRecommenderProvider provider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistory);
        provider.getRecommender(0, 1);
    }

    @Test
    public void testRecommenderHasNoUnderZeroLikelihoods() throws Exception {
        for (int i = 0; i < 100; i++) {
            recommender.registerSuccessiveItem(inventory.get(0).getId(), inventory.get(2).getId());
        }
        final double[][] dataSet = recommender.getDataSet().getData().getMatrix().toRawCopy2D();
        assert Arrays.stream(dataSet).noneMatch(row -> Arrays.stream(row).anyMatch(x -> x < 0));
    }

    private double getLikelihood(ItemBasedRecommender recommender, String item, String previousItem) throws NoSuchFieldException {
        return recommender.getDataSet().get(item, previousItem);
    }
}
