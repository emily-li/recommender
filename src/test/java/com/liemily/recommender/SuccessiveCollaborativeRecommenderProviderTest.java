package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.entity.UserHistory;
import com.liemily.exception.RecommenderException;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SuccessiveCollaborativeRecommenderProviderTest {
    private static Inventory inventory;
    private static DataSet dataSet;

    private SuccessiveCollaborativeRecommender recommender;

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

        final String item0Rec = recommender.getRecommendation(inventory.get(0).getId());
        assert (inventory.get(1).getId().equals(item0Rec)) || inventory.get(2).getId().equals(item0Rec);
        final String item3Rec = recommender.getRecommendation(inventory.get(3).getId());
        assert (inventory.get(1).getId().equals(item3Rec)) || inventory.get(2).getId().equals(item3Rec);
    }

    @Test
    public void testGetRecommenderGivenUserHistories() throws Exception {
        final UserHistory userHistory1 = new UserHistory(new String[]{inventory.get(0).getId()}, new String[]{inventory.get(2).getId()});
        final UserHistory userHistory2 = new UserHistory(new String[]{inventory.get(0).getId(), inventory.get(1).getId()}, new String[]{inventory.get(2).getId()});

        final SuccessiveCollaborativeRecommenderProvider recommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new Calculator(), userHistory1, userHistory2);
        recommender = recommenderProvider.getRecommender(1.01, 1.02);

        assertEquals(inventory.get(2).getId(), recommender.getRecommendation(inventory.get(0).getId()));
    }

    @Test
    public void testSuccessivePurchaseLikelihoodIncreases() throws Exception {
        String successiveItem = inventory.get(0).getId();
        String previousItem = inventory.get(1).getId();

        double[] prevLikelihoods = recommender.getDataSet().get(previousItem);
        recommender.registerSuccessiveItem(successiveItem, previousItem);
        double[] postLikelihoods = recommender.getDataSet().get(previousItem);

        assert prevLikelihoods[0] < postLikelihoods[0];
    }

    @Test
    public void testNotSuccessivePurchaseLikelihoodDecreases() throws Exception {
        String successiveItem = inventory.get(0).getId();
        String previousItem = inventory.get(1).getId();

        recommender.registerSuccessiveItem(successiveItem, previousItem);
        double[] prevLikelihoods = recommender.getDataSet().get(previousItem);
        recommender.registerSuccessiveItem(successiveItem, previousItem);
        double[] postLikelihoods = recommender.getDataSet().get(previousItem);

        for (int i = 1; i < inventory.getIds().length; i++) {
            assert prevLikelihoods[i] > postLikelihoods[i];
        }
    }

    @Test
    public void testRecentPurchaseLikelihoodIncreases() throws Exception {
        final double previousLikelihood0 = recommender.getDataSet().get(inventory.get(0).getId(), inventory.get(2).getId());
        final double previousLikelihood1 = recommender.getDataSet().get(inventory.get(1).getId(), inventory.get(2).getId());

        recommender.registerSuccessiveItem(inventory.get(2).getId(), inventory.get(1).getId(), inventory.get(0).getId());

        assert recommender.getDataSet().get(inventory.get(0).getId(), inventory.get(2).getId()) > previousLikelihood0;
        assert recommender.getDataSet().get(inventory.get(1).getId(), inventory.get(2).getId()) > previousLikelihood1;
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
}
