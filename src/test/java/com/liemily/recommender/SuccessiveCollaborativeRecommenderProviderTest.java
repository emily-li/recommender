package com.liemily.recommender;

import com.liemily.entity.*;
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
        inventory = new Inventory(new Item("fooItem0"), new Item("barItem1"), new Item("someItem2"), new Item("otherItem3"));
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

        final SuccessiveCollaborativeRecommenderProvider recommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new Calculator(), userHistory);
        recommender = recommenderProvider.getRecommender(1.01, 1.02);

        final String barRec = recommender.getRecommendation(inventory.get(1).getId());
        assert (inventory.get(0).getId().equals(barRec)) || inventory.get(3).getId().equals(barRec);
        final String someRec = recommender.getRecommendation(inventory.get(2).getId());
        assert (inventory.get(0).getId().equals(someRec)) || inventory.get(3).getId().equals(someRec);
    }

    @Test
    public void testGetRecommenderGivenUserHistories() throws Exception {
        final UserHistory userHistory1 = new UserHistory(new String[]{inventory.get(0).getId()}, new String[]{inventory.get(2).getId()});
        final UserHistory userHistory2 = new UserHistory(new String[]{inventory.get(0).getId(), inventory.get(1).getId()}, new String[]{inventory.get(2).getId()});

        final SuccessiveCollaborativeRecommenderProvider recommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new Calculator(), userHistory1, userHistory2);
        recommender = recommenderProvider.getRecommender(1.01, 1.02);

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
        final UserHistory userHistory = new UserHistory(new String[]{inventory.get(0).getId()}, new String[]{"invalidItem"});
        final SuccessiveCollaborativeRecommenderProvider provider = new SuccessiveCollaborativeRecommenderProvider(inventory, new Calculator(), userHistory);
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

    @Test
    public void testOnRealSubset() throws Exception {
        final InventoryService inventoryService = new InventoryService();
        final Inventory inventory = inventoryService.getInventory("src/test/resources/hybridSubsetInventory.txt");

        final UserHistoryService userHistoryService = new UserHistoryService();
        final UserHistory[] userHistories = userHistoryService.getUserHistories("src/test/resources/hybridSubsetUserHistories.txt");

        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new Calculator(), userHistories);
        final ItemBasedRecommender recommender = successiveCollaborativeRecommenderProvider.getRecommender(0.1, 0.2);
        //recommender.getDataSet().get()
        throw new UnsupportedOperationException();
    }

    private double getLikelihood(ItemBasedRecommender recommender, String item, String previousItem) throws NoSuchFieldException {
        return recommender.getDataSet().get(item, previousItem);
    }
}
