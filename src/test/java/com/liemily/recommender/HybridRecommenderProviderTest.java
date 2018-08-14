package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.Matrix;
import com.liemily.math.MatrixCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HybridRecommenderProviderTest {
    private static Inventory inventory;
    private static ItemBasedRecommender[] recommenders;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new Inventory(new Item("item1"), new Item("item2"), new Item("item3"));
        recommenders = new ItemBasedRecommender[]{
                new ItemBasedRecommender(new DataSet(inventory.getIds(), new Matrix(new double[][]{
                        new double[]{0.1, 0.2, 0.3},
                        new double[]{0.3, 0.2, 0.4},
                        new double[]{0.3, 0.2, 0.1}
                }))),
                new ItemBasedRecommender(new DataSet(inventory.getIds(), new Matrix(new double[][]{
                        new double[]{0.3, 0.2, 0.1},
                        new double[]{0.3, 0.3, 0.1},
                        new double[]{0.1, 0.2, 0.3}
                })))};
    }

    @Test
    public void testRecommendationFromHybrid() throws Exception {
        final HybridRecommenderProvider recommenderProvider = new HybridRecommenderProvider(inventory, recommenders, new MatrixCalculator());
        final String rec = recommenderProvider.getRecommender(0.0001, 0.0002).getRecommendation(inventory.get(0).getId());
        assertEquals(inventory.get(1).getId(), rec);
    }

    @Test
    public void testRecommenderHasNoZeroValuesIfDataSetHasEmptyValues() {
        final double[][] emptyDataSet = new double[inventory.getIds().length][inventory.getIds().length];
        Arrays.stream(emptyDataSet).forEach(row -> Arrays.fill(row, 0.0));
        final ItemBasedRecommender emptyRecommender = new ItemBasedRecommender(new DataSet(inventory.getIds(), new Matrix(emptyDataSet)));
        final HybridRecommenderProvider recommenderProvider = new HybridRecommenderProvider(inventory, new ItemBasedRecommender[]{recommenders[0], emptyRecommender}, new MatrixCalculator());

        final Matrix hybridMatrix = recommenderProvider.getRecommender(0.0001, 0.0002).getDataSet().getData();
        boolean hasNonZero = false;
        for (int i = 0; i < inventory.getIds().length; i++) {
            for (int j = 0; j < inventory.getIds().length; j++) {
                if (hybridMatrix.get(i, j) != 0) {
                    hasNonZero = true;
                    break;
                }
            }
        }

        assert hasNonZero;
    }
}
