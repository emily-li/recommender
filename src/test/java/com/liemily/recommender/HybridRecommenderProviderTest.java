package com.liemily.recommender;

import com.liemily.data.DataSet;
import com.liemily.data.Inventory;
import com.liemily.data.Item;
import com.liemily.math.MatrixCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HybridRecommenderProviderTest {
    private static Inventory inventory;
    private static ItemBasedRecommender[] recommenders;
    private HybridRecommenderProvider recommenderProvider;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new Inventory(new Item("item1"), new Item("item2"), new Item("item3"));
        recommenders = new ItemBasedRecommender[]{
                new ItemBasedRecommender(new DataSet(inventory.getIds(), new double[][]{
                        new double[]{0.1, 0.2, 0.3},
                        new double[]{0.3, 0.2, 0.4},
                        new double[]{0.3, 0.2, 0.1}
                })),
                new ItemBasedRecommender(new DataSet(inventory.getIds(), new double[][]{
                        new double[]{0.3, 0.2, 0.1},
                        new double[]{0.3, 0.3, 0.1},
                        new double[]{0.1, 0.2, 0.3}
                }))};
    }

    @Test
    public void testRecommendationFromHybrid() throws Exception {
        recommenderProvider = new HybridRecommenderProvider(inventory, recommenders, new MatrixCalculator());
        String rec = recommenderProvider.getRecommender().getRecommendation(inventory.get(0).getId());
        assertEquals(inventory.get(1).getId(), rec);
    }
}
