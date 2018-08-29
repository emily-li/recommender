package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class HybridRecommenderProviderTest {
    private static Inventory inventory;
    private static Calculator calculator;
    private static SuccessiveCollaborativeRecommender successiveCollaborativeRecommender;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new Inventory(new Item("item0"), new Item("item1"), new Item("item2"));
        calculator = new Calculator();
        successiveCollaborativeRecommender = new SuccessiveCollaborativeRecommender(new DataSet(inventory.getIds(), new Matrix(new double[][]{
                        new double[]{0.1, 0.2, 0.3},
                        new double[]{0.3, 0.2, 0.4},
                        new double[]{0.3, 0.2, 0.1}
        })),
                calculator);
    }

    @Test
    public void testRecommendationFromHybrid() throws Exception {
        final ItemBasedRecommender itemBasedRecommender = new ItemBasedRecommender(new DataSet(inventory.getIds(), new Matrix(new double[][]{
                new double[]{0.3, 0.2, 0.1},
                new double[]{0.3, 0.3, 0.1},
                new double[]{0.1, 0.2, 0.3}
        })));
        final HybridRecommenderProvider recommenderProvider = new HybridRecommenderProvider(inventory, calculator, successiveCollaborativeRecommender, itemBasedRecommender);
        final String rec = recommenderProvider.getRecommender(0.0001, 0.0002).getRecommendation(inventory.get(0).getId());
        assertEquals(inventory.get(0).getId(), rec);
    }

    @Test
    public void testRecommenderHasNoZeroValuesIfDataSetHasEmptyValues() {
        final double[][] emptyDataSet = new double[inventory.getIds().length][inventory.getIds().length];
        Arrays.stream(emptyDataSet).forEach(row -> Arrays.fill(row, 0.0));
        final ItemBasedRecommender emptyRecommender = new ItemBasedRecommender(new DataSet(inventory.getIds(), new Matrix(emptyDataSet)));
        final HybridRecommenderProvider recommenderProvider = new HybridRecommenderProvider(inventory, calculator, successiveCollaborativeRecommender, emptyRecommender);

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
